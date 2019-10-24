package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException 
	{
		
		BigInteger result = new BigInteger(); //initializes a BigInteger object
			integer = integer.trim(); // trims extra spaces etc.
	        if (integer.isEmpty()) throw new IllegalArgumentException(); // checks if input was entered 
	        for (int i = 0; i < integer.length(); i++)
	        {
	        	char f = integer.charAt(i);
	        	if (f != '+' && f != '-' && !Character.isDigit(f)) // input is incorrectly formatted
	        	{
	                throw new IllegalArgumentException(); 
	            }
	        }
	        
	        boolean negative = integer.charAt(0) == '-'; //stores whether integer is pos/neg
	        
	        if (integer.charAt(0) == '+' || integer.charAt(0) == '-') // cuts off positive/negative
	        {
	            integer = integer.substring(1);
	        }

	        if (checkZeros(integer)) // checks whether all digits are zero, BigInteger returned if so
	        {
	            return new BigInteger(); 
	        }

	        int numDigits = 0;

	        
	        for (int i = 0; i < integer.length(); i++) // deletes preceding zeros
	        {
	            if (integer.charAt(i) != '0') 
	            {
	                integer = integer.substring(i);
	                break;
	            }
	        }
	        DigitNode front = new DigitNode(Character.digit(integer.charAt(0), 10), null); //turns character into digit for add() and multiply() methods
	        for (int i = 1; i < integer.length(); i++) 
	        {
	            char value = integer.charAt(i);
	            front = new DigitNode(Character.digit(value, 10), front); //storing each digit into linked list
	            numDigits++;
	        }
	        
	        result.negative = negative;
	        result.front = front;
	        result.numDigits = numDigits + 1; //first element is added before loop runs
	        return result; 
		
	}
	        private static boolean checkZeros(String integer) //private method that checks if all digits entered are zero
	        {
	        	for (int i = 0; i < integer.length(); i++)
		        {
		        	char c = integer.charAt(i);
	                if (c != '0') 
	                {
	                    return false;
	                }
	            }
	            return true;
	        }
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		 if (first.front == null) //checks if lists are empty
		 {
            return second;
         }
		 if (second.front == null) 
		 {
            return first;
         }
		 
		 int s; //choice variable
		 
		 if (first.numDigits > second.numDigits) 
		 	{
	            s = 1;
	        } 
		 else if (second.numDigits > first.numDigits) 
	        {
	            s = -1;
	        } 
		 else 
	        {
	            for (int n = first.numDigits - 1; n >= 0; n--) 
	            {
	                DigitNode pointerOne = first.front;
	                DigitNode pointerTwo = second.front;
	                for (int i = 0; i < n; i++) 
	                {
	                	pointerOne = pointerOne.next;
	                	pointerTwo = pointerTwo.next;
	                }
	                if (pointerOne.digit != pointerTwo.digit) 
	                {
	                    if (pointerOne.digit > pointerTwo.digit) 
	                    {
	                    	s = 1;
	                    } else 
	                    {
	                        s = -1;
	                    }
	                }
	            }
	            s = 0; 
	        }
		 if (first.negative == second.negative) //based on lists sizes, they are swapped
		 {
	            if (s == 1) 
	            { // first is bigger
	                BigInteger placeHolder;
	                placeHolder = first;
	                first = second;
	                second = placeHolder;
	            }

	            DigitNode pointerOne = first.front;
				DigitNode pointerTwo = second.front;
	            DigitNode sumFront = new DigitNode(pointerOne.digit + pointerTwo.digit, null); //node for sum list
	            DigitNode pointer = sumFront;
	            for (int i = 1; i < second.numDigits; i++) 
	            {
									if (pointerOne != null) 
									{
										pointerOne = pointerOne.next;
									}
					pointerTwo = pointerTwo.next;
	                if (pointerOne == null) // if first has less digits and we ran out of them
	                { 
	                	pointer.next = new DigitNode(pointerTwo.digit, null);
	                	pointer = pointer.next;
	                } else 
	                {
	                	pointer.next = new DigitNode(pointerOne.digit + pointerTwo.digit, null);
	                	pointer = pointer.next;
	                }
	            }

	            boolean leftover = false;

	            pointer = sumFront;
	            for (int i = 0; i < second.numDigits; i++) 
	            {
	                if (pointer.digit >= 10) 
	                {
	                    if (i == second.numDigits - 1) 
	                    {
	                    	pointer.next = new DigitNode(1, null);
	                        leftover = true;
	                    } 
	                    else 
	                    {
	                    	pointer.next.digit += 1;
	                    }
	                    pointer.digit %= 10;
	                }
	                pointer = pointer.next;
	            }

	            BigInteger result = new BigInteger();
	            result.negative = first.negative;
	            result.front = sumFront;
							if (leftover) //adds extra number due to leftover
							{
								result.numDigits = second.numDigits + 1;
							} else 
							{
								result.numDigits = second.numDigits; 
							}
	            return result;
	        } 
		 else //if neg/pos signs aren't the same
	        { 
	            if (s == 0) // returns default valued big int
	            {
	                return new BigInteger(); 
	            }

	            
	            boolean switched = false;
	            if (s == 1) // second should be pos and >
	            {
	                BigInteger placeHolder;
	                placeHolder = first;
	                first = second;
	                second = placeHolder;
	            }

	            if (second.negative) // switches signs if second is > and neg
	            { 
	                first.negative = true;
	                second.negative = false;
	                switched = true;
	            }

	            DigitNode pointerOne = first.front;
	            DigitNode pointerTwo = second.front;
	            DigitNode sumFront = new DigitNode(pointerTwo.digit - pointerOne.digit, null);
	            DigitNode pointer = sumFront;
	            for (int i = 1; i < second.numDigits; i++) 
	            {
								if (pointerOne != null) 
								{
									pointerOne = pointerOne.next;
								}
								pointerTwo = pointerTwo.next;
	                if (pointerOne == null)
	                { 
	                    pointer.next = new DigitNode(pointerTwo.digit, null);
	                    pointer = pointer.next;
	                } else 
	                {
	                    pointer.next = new DigitNode(pointerTwo.digit - pointerOne.digit, null);
	                    pointer = pointer.next;
	                }
	            }

	            pointer = sumFront;
	            while (pointer != null) 
	            {
	                if (pointer.digit < 0) 
	                {
	                    pointer.digit += 10;
	                    pointer.next.digit -= 1;
	                }
	                pointer = pointer.next;
	            }

	            pointer = sumFront; 
	            int i = 0, j = -1;
	            while (pointer != null) // gets rid of extra zeros
	            {
	                if (pointer.digit != 0) 
	                {
	                    j = i;
	                }
	                i++;
	                pointer = pointer.next;
	            }

	            pointer = sumFront;
	            if (j != -1) 
	            {
	                for (i = 0; i < j; i++) //gets rid of trailing zeros
	                {
	                    pointer = pointer.next;
	                }
	                pointer.next = null; 
	            }

	            BigInteger result = new BigInteger();
	            result.numDigits = j + 1;
	            result.negative = switched;
	            result.front = sumFront;

	            return result;
	        }
      
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		if (first.front == null || second.front == null) 
		{
            return new BigInteger(); // if either of the lists are empty return defaulted bigint
        }

        BigInteger result = new BigInteger();
				int s;
		 
		 if (first.numDigits > second.numDigits) 
		 	{
	            s = 1;
	        } 
		 else if (second.numDigits > first.numDigits) 
	        {
	            s = -1;
	        } 
		 else 
	        {
	            for (int j = first.numDigits - 1; j >= 0; j--) 
	            {
	                DigitNode pointerOne = first.front;
	                DigitNode pointerTwo = second.front;
	                for (int i = 0; i < j; i++) 
	                {
	                	pointerOne = pointerOne.next;
	                    pointerTwo = pointerTwo.next;
	                }
	                if (pointerOne.digit != pointerTwo.digit) 
	                {
	                    if (pointerOne.digit > pointerTwo.digit) 
	                    {
	                    	s = 1;
	                    } 
	                    else 
	                    {
	                        s = -1;
	                    }
	                }
	            }
	            s = 0; 
	        }

				if (s == 1)
				{
					BigInteger placeHolder = first;
					first = second;
					second = placeHolder;
				}
        DigitNode pointerOne = first.front;
        for (int i = 0; i < first.numDigits; i++) // calls add since multiplication is repeated addition
        {
            int n = pointerOne.digit * ((int) Math.pow(10, i));
            for (int j = 0; j < n; j++) 
            {
                result = BigInteger.add(result, second);
            }
            pointerOne = pointerOne.next;
        }

        result.negative = first.negative != second.negative; 

        return result;	
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		if (front == null) 
		{
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}
