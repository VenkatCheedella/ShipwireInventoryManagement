
Design of the Project
========================

Three threads are spun, two threads run Order stream and one thread for Inventory Management.
A blocking queue is shared between streams and Inventory Management.
When the inventory is empty, Inventory Management thread is stopped. Inventory Management implements Callable 
interface. Hence, the return is captured to trigger a poison pill to the Order streams.
Blocking queue is cleared and order allocation is printed in the format as specified.

For further information on design and logic, you can reach me at venkatsubbu89@gmail.com

