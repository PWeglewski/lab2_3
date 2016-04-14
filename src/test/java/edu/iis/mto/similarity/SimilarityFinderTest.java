package edu.iis.mto.similarity;

import edu.iis.mto.search.SearchResult;
import edu.iis.mto.search.SequenceSearcher;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by piotr on 14.04.2016.
 */
public class SimilarityFinderTest {

    SimilarityFinder similarityFinder;

    private List<FuncInvocation> invocations;

    @Before
    public void before() {

        invocations = new ArrayList<>();

        similarityFinder = new SimilarityFinder(new SequenceSearcher() {
            public SearchResult search(int i, int[] ints) {
                final boolean isFound = Arrays.asList(ints).contains(i);

                invocations.add(new FuncInvocation(i, ints));

                SearchResult result = new SearchResult() {
                    public boolean isFound() {
                        return isFound;
                    }

                    public int getPosition() {
                        return 0;
                    }
                };

                return result;
            }
        });
    }

    @Test
    public void shouldReturnOneForEmptySequences() throws Exception {
        // given
        int seqOne[] = new int[0], seqTwo[] = new int[0];

        // when
        double result = similarityFinder.calculateJackardSimilarity(seqOne, seqTwo);

        // then
        assertThat(result, is(equalTo(1.0d)));
        assertThat(invocations.size(), is(equalTo(0)));
    }

    class FuncInvocation {
        public int[] seq;
        int i;

        public FuncInvocation(int i, int[] seq) {
            this.i = i;
            this.seq = seq;
        }
    }
}