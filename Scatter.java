import mpi.MPI;
import java.util.Arrays;

public class Scatter {
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int masSize = 10;
        int root = 0;
        int[] result = new int[masSize];
        if(masSize%size != 0){
            masSize += size - masSize%size;
        }
        int[] send = new int[masSize];
        int[] recv = new int[send.length / size];
        int[] recvResult;

        if (rank == root){
            for(int i = 0; i < send.length; i++)
                send[i] = i * (rank + 1);

            result = Arrays.copyOf(send, result.length);
            System.out.println("Send\n" + Arrays.toString(result));
        }

        MPI.COMM_WORLD.Scatter(send, 0, recv.length, MPI.INT, recv, 0, recv.length, MPI.INT, root);

        if(rank != size - 1){
            int a = send.length / size + send.length % size;
            recvResult = Arrays.copyOf(recv, a);
        }else {
            recvResult = Arrays.copyOf(recv, result.length % (size)+1);
        }
        System.out.println("Time: " + MPI.Wtime() +  " Rank: " + rank + "\n" + Arrays.toString(recvResult));
        MPI.Finalize();
    }
}
