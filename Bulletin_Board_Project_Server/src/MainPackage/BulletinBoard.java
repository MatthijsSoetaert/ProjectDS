package MainPackage;

import java.util.*;

public class BulletinBoard {
    private static List<Map<String,byte[]>> mailBoxes = new ArrayList<>();
    private static int size;

    public BulletinBoard(int size){
        BulletinBoard.size = size;
        for(int i = 0; i < size; i++){
            mailBoxes.add(new HashMap<>());
        }
      //  initFillBoard();
        System.out.println("");
    }

    private void initFillBoard(){
        for(Map<String,byte[]> mailbox : mailBoxes){
            for(int i = 0; i < 50; i++ ){
                byte[] tag = new byte[(int)(Math.random() * 100)];
                byte[] message = new byte[144];
                new Random().nextBytes(tag);
                new Random().nextBytes(message);
                mailbox.put(new String(tag),message);
            }
        }
    }

    public static List<Map<String,byte[]>> getMailBoxes(){
        return mailBoxes;
    }

    public static int getSize(){
        return size;
    }


}
