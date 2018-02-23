package codes.flyingdutchman.swt.tools;

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
}
