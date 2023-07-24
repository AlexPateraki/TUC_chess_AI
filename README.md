# TUC_chess_AI
The TUC-CHESS game is a variation of the classic chess game, with a 7x5 board and specific movement rules for pawns, rooks, and kings. Each player has 7 pawns, 2 rooks, and 1 king. The game ends when a king is captured, when only the two kings remain, or when the 14-minute time limit is exceeded. Players aim to achieve a higher score than their opponent by capturing pieces and earning bonuses. The game provides a graphical window to display messages and updates during gameplay.

The minimax search algorithm is used, along with a well-defined evaluation function and a pruning method. Enhance the search process by incorporating alpha-beta pruning. Further improve the search by implementing singular extensions, forward pruning, and other techniques.  

Additionally, the Monte Carlo Tree Search (MCTS) algorithm is implemented and is evaluated by its performance for different numbers of iterations.  

The communication between the server and clients is achieved using the UDP (User Datagram Protocol). Further details about this communication method will be presented in a tutorial during the course. It is recommended to verify how the server and client communication is established in the provided code. The communication strictly relies on the exchange of alphanumeric strings and is independent of the programming language used.

Some essential information:
The server's IP address is that of the local host. The server accepts messages on port 9876. The data packets sent and received by the server can have a maximum size of 200 bytes.
