import mpi.MPI;
import java.util.Random;
public class Reduce {
    private static Random rand =  new Random();
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int root = 0;
        int x;
        byte[] send = new byte[5];
        byte[] recv = new byte[send.length];
        if(rank == root)
            System.out.println("Отправляем ");
        MPI.COMM_WORLD.Barrier();
        for (int i = 0; i < send.length; i++) {
            x = rand.nextInt(8);
            send[i] = (byte) x;
        }
        print(send, "Rank " + rank);
        MPI.COMM_WORLD.Reduce(send,0,recv,0,send.length ,MPI.BYTE, MPI.MIN, root);
        MPI.COMM_WORLD.Barrier();
        if (rank == root) {
            print(recv, "Принято! Time: " + MPI.Wtime());
        }
        MPI.Finalize();
    }
    private static void print(byte[] mass, String st) {
        String str = "";
        for (int i = 0; i < mass.length; i++) {
          str += Integer.toString((int) mass[i], 2) + "\t";
        }
        str += "|\t";
        for (int i = 0; i < mass.length; i++) {
            str += mass[i] + "\t";
        }
        str += st;
        System.out.println(str);

    }
}
