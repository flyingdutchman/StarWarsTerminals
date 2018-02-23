package codes.flyingdutchman.swt.tools;

import sun.security.krb5.internal.crypto.RsaMd5CksumType;

import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class NFCTools {

    public static String read(CardChannel channel) {

        String s;
        ArrayList<Byte> arrayList = new ArrayList<>();

        try {
            //Tant que pas arrivé au bout
            byte cnt = 0x05;
            byte b = 0x00;
            boolean contin = true;
            while (contin) {
                ResponseAPDU response = channel.transmit(new CommandAPDU(new byte[]{(byte) 0xFF, (byte) 0xB0, (byte) 0x00, cnt, (byte) 0x04 }));
                byte[] res = response.getData();
                for(byte by : res) {
                    if(by == (byte)0xFE){
                        contin = false;
                        break;
                    }
                    arrayList.add(by);
                }

                if (response.getSW1() == 0x63 && response.getSW2() == 0x00)
                    System.out.println("Failed");
                cnt++;
            }

        } catch (CardException e) {
            e.printStackTrace();
        }

        byte[] arrayByte = new byte[arrayList.size()];
        for(int i = 5; i < arrayList.size(); i++)
            arrayByte[i] = arrayList.get(i);

        return new String(arrayByte, StandardCharsets.UTF_8);
    }

    public static void write(CardChannel channel, String s) {

        byte[] message = s.getBytes();
        int arrayLength = message.length+2;//+2 To host 6E and FE

        int arrayPad = 4 - arrayLength%4;

        byte[] formatedMessage = new byte[arrayLength+arrayPad];
        formatedMessage[0] = (byte) 0x6E; //Need it to say it's english for the phone (65 6E)
        formatedMessage[message.length+1] = (byte) 0xFE; //End

        for(int i = 0; i < message.length; i++) {
            formatedMessage[i+1] = message[i];
        }
        for(int i = message.length+2; i <formatedMessage.length-1; i++) {
            formatedMessage[i] = (byte) 0x00;
        }

        System.err.println(bin2hex(formatedMessage));

        byte payloadLength = (byte) (message.length + 3);
        byte recordLength = (byte) (payloadLength + 4 );
        byte NDEFHeader = (byte) 0xD1;
        byte recordType = (byte) 0x54;

        //RESET
        //response = channel.transmit(new CommandAPDU( new byte[] { (byte) 0xFF, (byte) 0xD6, (byte) 0x00, (byte) 0x04, (byte) 0x04, (byte) 0x03, (byte) 0x00, (byte) 0x0FE, (byte) 0x00 }));

        try {
            //BLOC 4 Bloc d'identification (pas exactement)
            ResponseAPDU response;
            response = channel.transmit(new CommandAPDU( new byte[] { (byte) 0xFF, (byte) 0xD6, (byte) 0x00, (byte) 0x04, (byte) 0x04, (byte) 0x03, recordLength, NDEFHeader, (byte) 0x01 }));
            if (response.getSW1() == 0x63 && response.getSW2() == 0x00)
                System.out.println("Failed");

            //BLOC 5 Bloc d'options (pas exactement)
            response = channel.transmit(new CommandAPDU( new byte[] { (byte) 0xFF, (byte) 0xD6, (byte) 0x00, (byte) 0x05, (byte) 0x04, payloadLength, recordType, (byte) 0x02, (byte) 0x65 }));
            if (response.getSW1() == 0x63 && response.getSW2() == 0x00)
                System.out.println("Failed");



            //BLOC 6
            /*response = channel.transmit(new CommandAPDU( new byte[] { (byte) 0xFF, (byte) 0xD6, (byte) 0x00, (byte) 0x06, (byte) 0x04, (byte) 0x6E, (byte) 0x63, (byte) 0x64, (byte) 0xFE }));
            if (response.getSW1() == 0x63 && response.getSW2() == 0x00)
                System.out.println("Failed");*/

            //Write all needed blocs
            //On par pas de 4 blocs et on évite de sortir
            for(int i = 0; i < formatedMessage.length; i += 4) {
                byte bloc = (byte) (0x06 + (byte)((double) i / (double) 4));

                //System.out.println("Bloc : "+String.format("%02X ", bloc));
                //System.out.println(String.format("%02X ", formatedMessage[i])+" "+String.format("%02X ", formatedMessage[i+1])+" "+String.format("%02X ", formatedMessage[i+2])+" "+String.format("%02X ", formatedMessage[i+3]));

                response = channel.transmit(new CommandAPDU( new byte[] { (byte) 0xFF, (byte) 0xD6, (byte) 0x00, bloc, (byte) 0x04, formatedMessage[i], formatedMessage[i+1], formatedMessage[i+2], formatedMessage[i+3] }));
            }

        } catch (CardException e) {
            e.printStackTrace();
        }

    }

    private static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));
    }
}
