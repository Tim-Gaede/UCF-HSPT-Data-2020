#include <cstdio>
#include <cstdlib>
#include <vector>
#include <queue>

#define MAX_N 100000

using namespace std;

int main(){
    int T, N, M, K;
    char labels[MAX_N+1];

    // input
    scanf("%d\n", &T);

    for(int t = 1; t <= T; t++){

        // more input
        scanf("%d %d %d", &N, &M, &K);

        // adj holds, for each house, its neighbors.
        // dist is distance from a particular color.
        // who is the house of a particular color closest to a house.
        vector<vector<int> > adj;
        vector<vector<int> > dist;
        vector<vector<int> > who;

        for(int n=0;n<N;n++){
            adj.push_back(vector<int>());
        }
        for(int c=0;c<26;c++){
            dist.push_back(vector<int>());
            who.push_back(vector<int>());
            for(int n=0;n<N;n++){
                // note that 999,999,999 is used as infinity.
                dist[c].push_back(999999999);
                who[c].push_back(-2);
            }
        }

        scanf("%s", labels);
        for(int m=0;m<M;m++){
            int a, b;
            scanf("%d %d", &a, &b);
            a--; b--;
            adj[a].push_back(b);
            adj[b].push_back(a);
        }

        // set the distances for all starts
        for(int n = 0; n < N; n++){
            dist[labels[n]-'A'][n] = 0;
        }


        // the plan:
        // get the distance from a color for each color.
        // this gives you a 26 by N matrix of distances.

        // while you BFS, you can also record which house is closest.
        for(int ch = 0; ch < 26; ch++){
            queue<int> queue;

            for(int n=0;n<N;n++){
                // put it onto the queue
                // but only if the character matches
                if(labels[n] == ch+'A'){
                    queue.push(n);
                    who[ch][n] = n;
                }
            }

            while(queue.size() > 0){
                int u = queue.front();
                queue.pop();
                for(int i = 0; i < adj[u].size(); i++){
                    int v = adj[u][i];
                    // visited?
                    if(dist[ch][v] == 999999999){
                        dist[ch][v] = dist[ch][u]+1;
                        who[ch][v] = who[ch][u];
                        queue.push(v);
                    }
                    // of a lower index?
                    else if(
                            dist[ch][u] + 1 == dist[ch][v] &&
                            who[ch][u] < who[ch][v]){
                        who[ch][v] = who[ch][u];
                    }
                }
            }
        }
        printf("Neighborhood #%d:\n", t);
        for(int k=0;k<K;k++){
            int q;
            char ch;

            scanf("%d %c", &q, &ch);

            printf("%d\n", 1+who[ch-'A'][q-1]);
        }
        printf("\n");
    }
}
