// Brett Fazio
// Solution to Lucky from UCF HSPT 2020.

#include <iostream>

int main(int argc, char **argv)
{
    // Take in the number of test cases.
    int t;
    std::cin >> t;

    for (int i = 0; i < t; i++)
    {
        // Take in the number of charms for this case.
        int n;
        std::cin >> n;

        // Go through each charm and if the charm is lucky (charm == 4)
        // add to a counter.
        int lucky_charms = 0;
        for (int j = 0; j < n; j++)
        {
            int charm;
            std::cin >> charm;

            if (charm == 4)
                lucky_charms++;
        }

        std::cout << lucky_charms << std::endl;
    }
}
