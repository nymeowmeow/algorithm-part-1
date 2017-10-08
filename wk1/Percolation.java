import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private int top;
    private int bottom;
    private WeightedQuickUnionUF unionfind;
    private boolean[] open;
    private int opencount = 0;

   //create n-by-n grid, with all sites blocked
    public Percolation(int n) 
    {
        if (n <= 0)
            throw new IllegalArgumentException("invalid size to Percolation constructor");
        this.size = n;
        this.unionfind = new WeightedQuickUnionUF(n*n+2);
        this.top = n*n;
        this.bottom = n*n+1;
        this.open = new boolean[n*n];
        //connect top virtual node
        for (int i = 1; i <= this.size; ++i)
        {
            int index = getIndex(1, i);
            this.unionfind.union(top, index);
        }
        //connect bottom virtual node
        for (int i = 1; i <= this.size; ++i)
        {
            int index = getIndex(size, i);
            this.unionfind.union(bottom, index);
        }
    }

    //open site (row i, column j) if it is not open already
    public void open(int i, int j)
    {
        if (isOpen(i, j))
            return;

        int index = getIndex(i, j);
        this.open[index] = true;
        this.opencount++;
        connectNeighbor(i, j, i-1, j);
        connectNeighbor(i, j, i+1, j);
        connectNeighbor(i, j, i, j-1);
        connectNeighbor(i, j, i, j+1);
    }

    private void connectNeighbor(int px, int py, int qx, int qy)
    {
        //px, py should be valid, but qx, qy location may be invalid
        if (qx < 1 || qx > this.size || qy < 1 || qy > this.size)
            return;

        //connect open neighbor
        if (!isOpen(qx, qy))
            return;

        int p = getIndex(px, py);
        int q = getIndex(qx, qy);
        this.unionfind.union(p, q);
    }

    //is site (row i, column j) open?
    public boolean isOpen(int i, int j)
    {
        int index = getIndex(i, j);

        return open[index];
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j)
    {
        if (!isOpen(i, j))
            return false;

        int index = getIndex(i, j);

         return this.unionfind.connected(top, index);
    }

    //does the system percolate?
    public boolean percolates()
    {
        return this.unionfind.connected(top, bottom);
    }

    private int getIndex(int i, int j)
    {
        if (i < 1 || j < 1 || i > size || j > size)
            throw new IndexOutOfBoundsException("Invalid input row, col");

        return (i-1)*this.size + (j-1);
    }
}
