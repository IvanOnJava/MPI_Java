import mpi.MPI;
import java.util.Arrays;
import java.util.Random;
public class Scatter_Gather_Sort_Array{
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int N = 4;
        int M = 15;
        int root = 0;
        if(rank == root)
            System.out.println("Количество процессов: " + size);
        byte[][] send = new byte[N][M];
        byte[] recv = new byte[M];
        MPI.COMM_WORLD.Barrier();
        if (rank == root) {
            System.out.println("Отправляем");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    send[i][j] = (byte) (1 + new Random().nextInt(10));
                    System.out.print(send[i][j] + "\t");
                }
                System.out.println();
            }
            System.out.println("Сортируем");
        }
        if (rank == root) {
            if (size >= N)
                for (int i = 0; i < N; i++)
                    MPI.COMM_WORLD.Isend(send[i], 0, M, MPI.BYTE, i, 1);
            if(size == 1){
                   long a = System.currentTimeMillis();
                    for (int i = 0; i < N; i++) {
                        Arrays.sort(send[i]);
                        System.out.println(Arrays.toString(send[i]) + " Rank: " + rank + ", Time: " + (System.currentTimeMillis() - a));
                    }
                    System.out.println("Собираем");
                    for (int i = 0; i < N; i++) {
                        for (int j = 0; j < M; j++) {
                            System.out.print(send[i][j] + "\t");
                        }
                        System.out.println();
                    }
                    MPI.Finalize();
                    return;
            }else{
                int count = 0;
                for(int i = 0; i < 2; i++)
                    MPI.COMM_WORLD.Isend(send[count++], 0, M, MPI.BYTE, i, 2);
                for(int i = 0; i < 2; i++)
                    MPI.COMM_WORLD.Isend(send[count++], 0, M, MPI.BYTE, i, 3);
            }
        }
        if(size >= N) {
            long a = System.currentTimeMillis();
            MPI.COMM_WORLD.Recv(recv, 0, M, MPI.BYTE, 0, 1);
            Arrays.sort(recv);
            System.out.println(Arrays.toString(recv) + " Rank: " + rank + ", Time: " + (System.currentTimeMillis() - a));
            for (int i = 0; i < N; i++)
                MPI.COMM_WORLD.Isend(recv, 0, M, MPI.BYTE, i, 1);

            if (rank == root) {
                for (int i = 0; i < N; i++)
                    MPI.COMM_WORLD.Recv(send[i], 0, M, MPI.BYTE, i, 1);
            }

            if (rank == root) {
                System.out.println("Собираем ");
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < M; j++) {
                        System.out.print(send[i][j] + "\t");
                    }
                    System.out.println();
                }
            }
        }
        MPI.COMM_WORLD.Barrier();
        if(size < N){
                long a = System.currentTimeMillis();
                MPI.COMM_WORLD.Recv(recv, 0, M, MPI.BYTE, 0, 2);
                Arrays.sort(recv);
                System.out.println(Arrays.toString(recv) + " Rank: " + rank + ", Time: " + (System.currentTimeMillis() - a));
                MPI.COMM_WORLD.Isend(recv, 0, M, MPI.BYTE, 0, 2);

                MPI.COMM_WORLD.Recv(recv, 0, M, MPI.BYTE, 0, 3);
                Arrays.sort(recv);
                System.out.println(Arrays.toString(recv) + " Rank: " + rank + ", Time: " + (System.currentTimeMillis() - a));
                MPI.COMM_WORLD.Isend(recv, 0, M, MPI.BYTE, 0, 3);
                if(rank == root){
                    int count2 = 0;
                    for (int i = 0; i < 2; i++)
                        MPI.COMM_WORLD.Recv(send[count2++], 0, M, MPI.BYTE, i, 2);

                    for (int i = 0; i < 2; i++)
                        MPI.COMM_WORLD.Recv(send[count2++], 0, M, MPI.BYTE, i, 3);
                    System.out.println("Собираем");
                    for (int i = 0; i < N; i++) {
                        for (int j = 0; j < M; j++) {
                            System.out.print(send[i][j] + "\t");
                        }
                        System.out.println();
                    }
                }
        }
        MPI.Finalize();
    }
}
