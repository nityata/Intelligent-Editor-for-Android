package hr.com.again;

public class KohonenNetwork extends Network{

	double outputWeights[][];

	  /**
	   * The learning method.
	   */
	  protected int learnMethod = 1;

	  /**
	   * The learning rate.
	   */
	  protected double learnRate = 0.3;

	  /**
	   * Abort if error is beyond this
	   */
	  protected double quitError = 0.1;

	  /**
	   * How many retries before quit.
	   */
	  protected int retries = 10000;

	  /**
	   * Reduction factor.
	   */
	  protected double reduction = .99;

	  /**
	   * The owner object, to report to.
	   */
	  protected Main owner;

	  /**
	   * Set to true to abort learning.
	   */
	  public boolean halt = false;

	  /**
	   * The training set.
	   */
	  protected TrainingSet train;

	  /**
	   * The constructor.
	   * 
	   * @param inputCount
	   *          Number of input neurons
	   * @param outputCount
	   *          Number of output neurons
	   * @param owner
	   *          The owner object, for updates.
	   */
	  public KohonenNetwork(int inputCount, int outputCount, Main owner)
	  {
	    

	    totalError = 1.0;

	    this.inputNeuronCount = inputCount;
	    this.outputNeuronCount = outputCount;
	    this.outputWeights = new double[outputNeuronCount][inputNeuronCount + 1];
	    this.output = new double[outputNeuronCount];
	    this.owner = owner;
	  }
	  
	  public void setTrainingSet(TrainingSet set)
	  {
	    train = set;
	  }

	  /**
	   * Copy the weights from this network to another.
	   * 
	   * @param dest
	   *          The destination for the weights.
	   * @param source
	   */
	  public static void copyWeights(KohonenNetwork dest, KohonenNetwork source)
	  {
	    for (int i = 0; i < source.outputWeights.length; i++)
	    {
	      System.arraycopy(source.outputWeights[i], 0, dest.outputWeights[i], 0,
	          source.outputWeights[i].length);
	    }
	  }
	  
	  public void clearWeights()
	  {
	    totalError = 1.0;
	    for (int y = 0; y < outputWeights.length; y++)
	      for (int x = 0; x < outputWeights[0].length; x++)
	        outputWeights[y][x] = 0;
	  }
	  
	  void normalizeInput(final double input[], double normfac[], double synth[])
	  {
	    double length;

	    length = vectorLength(input);
	    // just in case it gets too small
	    if (length < 1.E-30)
	      length = 1.E-30;

	    normfac[0] = 1.0 / Math.sqrt(length);
	    synth[0] = 0.0;

	  }
	  
	  void normalizeWeight(double w[])
	  {
	    int i;
	    double len;

	    len = vectorLength(w);
	    // just incase it gets too small
	    if (len < 1.E-30)
	      len = 1.E-30;

	    len = 1.0 / Math.sqrt(len);
	    for (i = 0; i < inputNeuronCount; i++)
	      w[i] *= len;
	    w[inputNeuronCount] = 0;

	  }
	  
	  public int winner(double input[], double normfac[], double synth[])
	  {
	    int i, win = 0;
	    double biggest, optr[];

	    normalizeInput(input, normfac, synth); // Normalize input

	    biggest = -1.E30;
	    for (i = 0; i < outputNeuronCount; i++)
	    {
	      optr = outputWeights[i];
	      output[i] = dotProduct(input, optr) * normfac[0] + synth[0]
	          * optr[inputNeuronCount];
	      // Remap to bipolar(-1,1 to 0,1)
	      output[i] = 0.5 * (output[i] + 1.0);
	      if (output[i] > biggest)
	      {
	        biggest = output[i];
	        win = i;
	      }
	      // account for rounding
	      if (output[i] > 1.0)
	        output[i] = 1.0;
	      if (output[i] < 0.0)
	        output[i] = 0.0;
	    }

	    return win;
	  }
	  
	  void evaluateErrors(double rate, int learn_method, int won[],
		      double bigerr[], double correc[][], double work[])
		      throws RuntimeException
		  {
		    int best,  tset;
		    double dptr[], normfac[] = new double[1];
		    double synth[] = new double[1], cptr[], wptr[], length, diff;

		    // reset correction and winner counts

		    for (int y = 0; y < correc.length; y++)
		    {
		      for (int x = 0; x < correc[0].length; x++)
		      {
		        correc[y][x] = 0;
		      }
		    }

		    for (int i = 0; i < won.length; i++)
		      won[i] = 0;

		    bigerr[0] = 0.0;
		    // loop through all training sets to determine correction
		    for (tset = 0; tset < train.getTrainingSetCount(); tset++)
		    {
		      dptr = train.getInputSet(tset);
		      best = winner(dptr, normfac, synth);
		      won[best]++;
		      wptr = outputWeights[best];
		      cptr = correc[best];
		      length = 0.0;

		      for (int i = 0; i < inputNeuronCount; i++)
		      {
		        diff = dptr[i] * normfac[0] - wptr[i];
		        length += diff * diff;
		        if (learn_method != 0)
		          cptr[i] += diff;
		        else
		          work[i] = rate * dptr[i] * normfac[0] + wptr[i];
		      }
		      diff = synth[0] - wptr[inputNeuronCount];
		      length += diff * diff;
		      if (learn_method != 0)
		        cptr[inputNeuronCount] += diff;
		      else
		        work[inputNeuronCount] = rate * synth[0] + wptr[inputNeuronCount];

		      if (length > bigerr[0])
		        bigerr[0] = length;

		      if (learn_method == 0)
		      {
		        normalizeWeight(work);
		        for (int i = 0; i <= inputNeuronCount; i++)
		          cptr[i] += work[i] - wptr[i];
		      }

		    }

		    bigerr[0] = Math.sqrt(bigerr[0]);
		  }
	  
	  void adjustWeights(double rate, int learn_method, int won[],
		      double bigcorr[], double correc[][])

		  {
		    double corr, cptr[], wptr[], length, f;

		    bigcorr[0] = 0.0;

		    for (int i = 0; i < outputNeuronCount; i++)
		    {

		      if (won[i] == 0)
		        continue;

		      wptr = outputWeights[i];
		      cptr = correc[i];

		      f = 1.0 / (double) won[i];
		      if (learn_method != 0)
		        f *= rate;

		      length = 0.0;

		      for (int j = 0; j <= inputNeuronCount; j++)
		      {
		        corr = f * cptr[j];
		        wptr[j] += corr;
		        length += corr * corr;
		      }

		      if (length > bigcorr[0])
		        bigcorr[0] = length;
		    }
		    // scale the correction
		    bigcorr[0] = Math.sqrt(bigcorr[0]) / rate;
		  }
	  
	  void forceWin(int won[]) throws RuntimeException
	  {
	    int i, tset, best, size, which = 0;
	    double dptr[], normfac[] = new double[1];
	    double synth[] = new double[1], dist, optr[];

	    size = inputNeuronCount + 1;

	    dist = 1.E30;
	    for (tset = 0; tset < train.getTrainingSetCount(); tset++)
	    {
	      dptr = train.getInputSet(tset);
	      best = winner(dptr, normfac, synth);
	      if (output[best] < dist)
	      {
	        dist = output[best];
	        which = tset;
	      }
	    }

	    dptr = train.getInputSet(which);
	    best = winner(dptr, normfac, synth);

	    dist = -1.e30;
	    i = outputNeuronCount;
	    while ((i--) > 0)
	    {
	      if (won[i] != 0)
	        continue;
	      if (output[i] > dist)
	      {
	        dist = output[i];
	        which = i;
	      }
	    }

	    optr = outputWeights[which];

	    System.arraycopy(dptr, 0, optr, 0, dptr.length);

	    optr[inputNeuronCount] = synth[0] / normfac[0];
	    normalizeWeight(optr);
	  }
	  
	  public void initialize()
	  {
	    int i;
	    double optr[];

	    clearWeights();
	    randomizeWeights(outputWeights);
	    for (i = 0; i < outputNeuronCount; i++)
	    {
	      optr = outputWeights[i];
	      normalizeWeight(optr);
	    }
	  }
	
	@Override
	public void learn() throws RuntimeException {
		// TODO Auto-generated method stub
		   int i,  tset, iter, n_retry;
		    int won[], winners;
		    double work[], correc[][], rate, best_err, dptr[];
		    double bigerr[] = new double[1];
		    double bigcorr[] = new double[1];
		    KohonenNetwork bestnet; // Preserve best here

		    totalError = 1.0;

		    for (tset = 0; tset < train.getTrainingSetCount(); tset++)
		    {
		      dptr = train.getInputSet(tset);
		      if (vectorLength(dptr) < 1.E-30)
		      {
		        throw (new RuntimeException(
		            "Multiplicative normalization has null training case"));
		      }

		    }

		    bestnet = new KohonenNetwork(inputNeuronCount, outputNeuronCount, owner);

		    won = new int[outputNeuronCount];
		    correc = new double[outputNeuronCount][inputNeuronCount + 1];
		    if (learnMethod == 0)
		      work = new double[inputNeuronCount + 1];
		    else
		      work = null;

		    rate = learnRate;

		    initialize();
		    best_err = 1.e30;

		    // main loop:

		    n_retry = 0;
		    for (iter = 0;; iter++)
		    {

		      evaluateErrors(rate, learnMethod, won, bigerr, correc, work);

		      totalError = bigerr[0];

		      if (totalError < best_err)
		      {
		        best_err = totalError;
		        copyWeights(bestnet, this);
		      }

		      winners = 0;
		      for (i = 0; i < won.length; i++)
		        if (won[i] != 0)
		          winners++;

		      if (bigerr[0] < quitError)
		        break;

		      if ((winners < outputNeuronCount)
		          && (winners < train.getTrainingSetCount()))
		      {
		        forceWin(won);
		        continue;
		      }

		      adjustWeights(rate, learnMethod, won, bigcorr, correc);

		      // owner.updateStats(n_retry,totalError,best_err);
		      if (halt)
		      {
		        // owner.updateStats(n_retry,totalError,best_err);
		        break;
		      }
		      Thread.yield();

		      if (bigcorr[0] < 1E-5)
		      {
		        if (++n_retry > retries)
		          break;
		        initialize();
		        iter = -1;
		        rate = learnRate;
		        continue;
		      }

		      if (rate > 0.01)
		        rate *= reduction;

		    }

		    // done

		    copyWeights(this, bestnet);

		    for (i = 0; i < outputNeuronCount; i++)
		      normalizeWeight(outputWeights[i]);

		    halt = true;
		    n_retry++;
		    // owner.updateStats(n_retry,totalError,best_err);

	}

	@Override
	void trial(double[] input) {
		// TODO Auto-generated method stub
		int i;
	    double normfac[] = new double[1], synth[] = new double[1], optr[];

	    normalizeInput(input, normfac, synth);

	    for (i = 0; i < outputNeuronCount; i++)
	    {
	      optr = outputWeights[i];
	      output[i] = dotProduct(input, optr) * normfac[0] + synth[0]
	          * optr[inputNeuronCount];
	      // Remap to bipolar (-1,1 to 0,1)
	      output[i] = 0.5 * (output[i] + 1.0);
	      // account for rounding
	      if (output[i] > 1.0)
	        output[i] = 1.0;
	      if (output[i] < 0.0)
	        output[i] = 0.0;
	    }
	}

}
