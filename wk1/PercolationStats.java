import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats
{
    private double mean = 0.0;
    private double stddev = 0.0;
    private double confidencelo = 0.0;
    private double confidencehi = 0.0;

    //perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("invalid input");

        double[] results = new double[trials];
        for (int i = 0; i < trials; ++i)
        {
            Percolation p = new Percolation(n);
            while (!p.percolates())
            {
                int ii = StdRandom.uniform(n)+1;
                int jj = StdRandom.uniform(n)+1;
                p.open(ii, jj);
            }
            //get results
            int count = 0;
            for (int ii = 1; ii <= n; ++ii)
                for (int jj = 1; jj <= n; ++jj)
                {
                    if (p.isOpen(ii, jj))
                        ++count;
                }
            double opencount = (count*0.1)/(n*n);
            results[i] = opencount;
	}
        this.mean = StdStats.mean(results);
        this.stddev = StdStats.stddev(results);
        this.confidencelo = this.mean - 1.96*this.stddev/Math.sqrt(trials);
        this.confidencehi = this.mean + 1.96*this.stddev/Math.sqrt(trials);
    }

    //sample mean of percolation threshold
    public double mean()
    {
        return this.mean;
    }

    //sample standard deviation of percolation threshold
    public double stddev()
    {
        return this.stddev;
    }

    //low  endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return this.confidencelo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return this.confidencehi;
    }

    public static void main(String[] args)
    {
        PercolationStats stat = new PercolationStats(200, 100);
        System.out.println("Mean is : " + stat.mean());
        System.out.println("Stddev is : " + stat.stddev());
        System.out.println("95% confidence interval is " + stat.confidenceLo() + ", " + stat.confidenceHi());
    }
}
