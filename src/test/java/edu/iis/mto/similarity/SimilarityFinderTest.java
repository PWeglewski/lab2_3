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
                boolean foundFlag = false;
                for (int j : ints){
                    if(i==j){
                        foundFlag = true;
                        break;
                    }
                }
                final boolean isFound = foundFlag;

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

    @Test
    public void shouldReturnCorrectResultForNonEmptySequences() throws Exception {
        // given
        int seqOne[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int seqTwo[] = {1, 3, 5, 7, 9};

        // when
        double result = similarityFinder.calculateJackardSimilarity(seqOne, seqTwo);

        // then
        assertThat(result, is(equalTo(0.5d)));
        assertThat(invocations.size(), is(equalTo(seqOne.length)));
        assertThat(countInvocations(1, seqTwo), is(equalTo(1)));
        assertThat(countInvocations(2, seqTwo), is(equalTo(1)));
        assertThat(countInvocations(3, seqTwo), is(equalTo(1)));
        assertThat(countInvocations(4, seqTwo), is(equalTo(1)));
        assertThat(countInvocations(5, seqTwo), is(equalTo(1)));
        assertThat(countInvocations(6, seqTwo), is(equalTo(1)));
        assertThat(countInvocations(7, seqTwo), is(equalTo(1)));
        assertThat(countInvocations(8, seqTwo), is(equalTo(1)));
        assertThat(countInvocations(9, seqTwo), is(equalTo(1)));
        assertThat(countInvocations(10, seqTwo), is(equalTo(1)));
    }

    class FuncInvocation {
        public int[] seq;
        int i;

        public FuncInvocation(int i, int[] seq) {
            this.i = i;
            this.seq = seq;
        }
    }

    public int countInvocations(int i, int[] seq){
        int count = 0;

        for (FuncInvocation invocation : invocations){
            if(invocation.i==i&&invocation.seq==seq)
                count++;
        }

        return count;
    }
}