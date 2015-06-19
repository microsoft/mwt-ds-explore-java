package com.mwt.sample;

import com.mwt.contexts.SimpleContext;
import com.mwt.explorers.*;
import com.mwt.misc.Feature;
import com.mwt.policies.Policy;
import com.mwt.recorders.StringRecorder;
import com.mwt.scorers.Scorer;

import java.util.ArrayList;
import java.util.List;

public class ExploreOnlySample {

  public static void main(String[] args) {
    String explorationType = args[0];
    if (explorationType.equals("greedy")) {
      // Initialize Epsilon-Greedy explore algorithm using built-in StringRecorder and SimpleContext types

      // Creates a recorder of built-in StringRecorder type for string serialization
      StringRecorder<SimpleContext> recorder = new StringRecorder<SimpleContext>();

      // Creates an MwtExplorer instance using the recorder above
      MwtExplorer<SimpleContext> mwtt = new MwtExplorer<SimpleContext>("mwt", recorder);

      // Creates a policy that interacts with SimpleContext type
      StringPolicy policy = new StringPolicy();

      int numActions = 10;
      float epsilon = 0.2f;
      // Creates an Epsilon-Greedy explorer using the specified settings
      EpsilonGreedyExplorer<SimpleContext> explorer = new EpsilonGreedyExplorer<SimpleContext>(policy, epsilon, numActions);

      // Creates a context of built-in SimpleContext type
      ArrayList<Feature> features = new ArrayList<Feature>();
      features.add(new Feature(1, 0.5f));
      features.add(new Feature(4, 1.3f));
      features.add(new Feature(9, -0.5f));
      SimpleContext context = new SimpleContext(features);

      // Performs exploration by passing an instance of the Epsilon-Greedy exploration algorithm into MwtExplorer
      // using a sample string to uniquely identify this event
      String uniqueKey = "eventid";
      mwtt.chooseAction(explorer, uniqueKey, context);

      System.out.println(recorder.getRecording());
    } else if (explorationType.equals("tau-first")) {
      // Initialize Tau-First explore algorithm using custom Recorder, Policy & Context types
      MyRecorder recorder = new MyRecorder();
      MwtExplorer<MyContext> mwtt = new MwtExplorer<MyContext>("mwt", recorder);

      int numActions = 10;
      int tau = 0;
      MyPolicy policy = new MyPolicy();
      mwtt.chooseAction(new TauFirstExplorer<MyContext>(policy, tau, numActions), "key", new MyContext());

      StringBuilder out = new StringBuilder();
      for (Interaction<MyContext> interaction : recorder.getAllInteractions()) {
        out.append(interaction.action).append(",");
      }

      System.out.println(out.toString());
    } else if (explorationType.equals("bootstrap")) {
      // Initialize Bootstrap explore algorithm using custom Recorder, Policy & Context types
      MyRecorder recorder = new MyRecorder();
      MwtExplorer<MyContext> mwtt = new MwtExplorer<MyContext>("mwt", recorder);

      int numActions = 10;
      int numbags = 2;
      List<Policy<MyContext>> policies = new ArrayList<Policy<MyContext>>();
      for (int i = 0; i < numbags; i++)
      {
        policies.add(new MyPolicy(i * 2));
      }
      mwtt.chooseAction(new BootstrapExplorer<MyContext>(policies, numActions), "key", new MyContext());

      StringBuilder out = new StringBuilder();
      for (Interaction<MyContext> interaction : recorder.getAllInteractions()) {
        out.append(interaction.action).append(",");
      }

      System.out.println(out.toString());
    } else if (explorationType.equals("softmax")) {
      // Initialize Softmax explore algorithm using custom Recorder, Scorer & Context types
      MyRecorder recorder = new MyRecorder();
      MwtExplorer<MyContext> mwtt = new MwtExplorer<MyContext>("mwt", recorder);

      int numActions = 10;
      float lambda = 0.5f;
      MyScorer scorer = new MyScorer(numActions);
      mwtt.chooseAction(new SoftmaxExplorer<MyContext>(scorer, lambda, numActions), "key", new MyContext());

      StringBuilder out = new StringBuilder();
      for (Interaction<MyContext> interaction : recorder.getAllInteractions()) {
        out.append(interaction.action).append(",");
      }

      System.out.println(out.toString());
    } else if (explorationType.equals("generic")) {
      // Initialize Generic explore algorithm using custom Recorder, Scorer & Context types
      MyRecorder recorder = new MyRecorder();
      MwtExplorer<MyContext> mwtt = new MwtExplorer<MyContext>("mwt", recorder);

      int numActions = 10;
      MyScorer scorer = new MyScorer(numActions);
      mwtt.chooseAction(new GenericExplorer<MyContext>(scorer, numActions), "key", new MyContext());

      StringBuilder out = new StringBuilder();
      for (Interaction<MyContext> interaction : recorder.getAllInteractions()) {
        out.append(interaction.action).append(",");
      }

      System.out.println(out.toString());
    } else {
      // Add error here
    }
  }
}
