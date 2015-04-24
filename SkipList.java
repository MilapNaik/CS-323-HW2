/* Skeleton written by professor
   Actual methods written by Milap Naik */

//Note to self: Change these horrible variable names

import java.util.*;


public class SkipList
{
  public SkipListEntry head;    // First element of the top level
  public SkipListEntry tail;    // Last element of the top level


  public int number; 		// number of entries in the Skip list

  public int height;       // Height
  public Random rando;    // Coin toss

  /* ----------------------------------------------
     Constructor: empty skiplist

                          null        null
                           ^           ^
                           |           |
     head --->  null <-- -inf <----> +inf --> null
                           |           |
                           v           v
                          null        null
     ---------------------------------------------- */
  public SkipList()     // Default constructor...
  { 
     SkipListEntry p1, p2;

     p1 = new SkipListEntry(SkipListEntry.negInf, null);
     p2 = new SkipListEntry(SkipListEntry.posInf, null);

     head = p1;
     tail = p2;

     p1.right = p2;
     p2.left = p1;

     number = 0;

     height = 0;
     rando = new Random(1234);    // Always generate the same set of random numbers.
  }


  /** Returns the number of entries in the hash table. */
  public int size() 
  { 
    return number; 
  }

  /** Returns whether or not the table is empty. */
  public boolean isEmpty() 
  { 
    return (number == 0); 
  }

  /* ------------------------------------------------------
     findEntry(k): find the largest key x <= k
		   on the LOWEST level of the Skip List
     ------------------------------------------------------ */
  public SkipListEntry findEntry(String k)
  {
     SkipListEntry p;

     /* -----------------
	Start at "head"
	----------------- */
     p = head;


     while ( true )
     {
        /* --------------------------------------------
	   Search RIGHT until you find a LARGER entry
	   -------------------------------------------- */
        while ( p.right.key != SkipListEntry.posInf && 
		k.compareTo(p.right.key) >= 0 )
	{
           p = p.right;
	}

	/* ---------------------------------
	   Go down one level if you can...
	   --------------------------------- */
	if ( p.down != null )
        {  
           p = p.down;
        }
        else
	   break;	// We reached the LOWEST level... Exit...
     }

     return(p);         // Nearest p (<= k)
  }


  /** Returns the value associated with a key. */
  public Integer get (String k) 
  {
     SkipListEntry p;

     p = findEntry(k);

     if ( k.equals( p.getKey() ) )
        return(p.value);
     else
        return(null);
  }

  /* ------------------------------------------------------------------
     insertAfterAbove(p, q, y=(k,v) )
 
        1. create new entry (k,v)
	2. insert (k,v) AFTER p
	3. insert (k,v) ABOVE q

             p <--> (k,v) <--> p.right
                      ^
		      |
		      v
		      q

      Returns the reference of the newly created (k,v) entry
     ------------------------------------------------------------------ */
  public SkipListEntry insertAfterAbove(SkipListEntry p, SkipListEntry q, 
                                         String k)
  {
     SkipListEntry e;

     e = new SkipListEntry(k, null);

     /* ---------------------------------------
	Use the links before they are changed !
	--------------------------------------- */
     e.left = p;
     e.right = p.right;
     e.down = q;

     /* ---------------------------------------
	Now update the existing links..
	--------------------------------------- */
     p.right.left = e;
     p.right = e;
     q.up = e;

     return(e);
  }

  /** Put a key-value pair in the map, replacing previous one if it exists. */
  public Integer put (String k, Integer v) 
  {
     SkipListEntry p, q;
     int       i;

     p = findEntry(k);

//   System.out.println("findEntry(" + k + ") returns: " + p.key);
     /* ------------------------
	Check if key is found
	------------------------ */
     if ( k.equals( p.getKey() ) )
     {
        Integer old = p.value;

	p.value = v;

    	return(old);
     }

     /* ------------------------
	Insert new entry (k,v)
	------------------------ */

     /* ------------------------------------------------------
        **** BUG: He forgot to insert in the lowest level !!!
	Link at the lowest level
	------------------------------------------------------ */
     q = new SkipListEntry(k, v);
     q.left = p;
     q.right = p.right;
     p.right.left = q;
     p.right = q;

     i = -1;

     while ( rando.nextDouble() < 0.5 )
     {
        i = i + 1;

//	System.out.println("i = " + i + ", h = " + h );

	/* ---------------------------------------------
	   Check if height exceed current height.
 	   If so, make a new EMPTY level
	   --------------------------------------------- */
        if ( i >= height )
   	{
           SkipListEntry p1, p2;

	   height = height + 1;

           p1 = new SkipListEntry(SkipListEntry.negInf,null);
           p2 = new SkipListEntry(SkipListEntry.posInf,null);
   
	   p1.right = p2;
	   p1.down  = head;

	   p2.left = p1;
	   p2.down = tail;

	   head.up = p1;
	   tail.up = p2;

	   head = p1;
	   tail = p2;
	}


	/* -------------------------
	   Scan backwards...
	   ------------------------- */
	while ( p.up == null )
	{
	   p = p.left;
	}

	p = p.up;


	/* ---------------------------------------------
           Add one more (k,v) to the column
	   --------------------------------------------- */
   	SkipListEntry e;
   		 
   	e = new SkipListEntry(k, null);  // Don't need the value...
   		 
   	/* ---------------------------------------
   	   Initialize links of e
   	   --------------------------------------- */
   	e.left = p;
   	e.right = p.right;
   	e.down = q;
   		 
   	/* ---------------------------------------
   	   Change the neighboring links..
   	   --------------------------------------- */
   	p.right.left = e;
   	p.right = e;
   	q.up = e;

        q = e;		// Set q up for the next iteration

     }

     number = number + 1;

     return(null);   // No old value
  }

  // ***********************************************************
  // You need to write these method !!!
  // ***********************************************************

  /** Removes the key-value pair with a specified key. */

  public Integer remove (String key) 
  {
	  SkipListEntry Entry = findEntry(key), rem = Entry.up;
	  int val = Entry.getValue();
	  
	  if (Entry.key != key)
		  return null; //Not found
		  
	  while (rem != null){ 
		  Entry.left.right = Entry.right;  //deletes entry
		  Entry.right.left = Entry.left;   //deletes entry
	      rem = Entry.up;                  //moves up in skiplist
	      Entry = rem;   
		  }
	  
	  number--;
	  
	  return (val);
  }


  public SkipListEntry firstEntry()
  {
	  SkipListEntry bottom = head, first;
	  if (number==0)
		  return null;  //nothing in the skiplist
	  while (bottom.down != null){
		  bottom = bottom.down;
	  }
	  first = bottom.right;
	  return (first);
     
  }

  public SkipListEntry lastEntry()
  {
	  SkipListEntry bottom = tail, last;
	  if (number==0)
		  return null;  //nothing in the skiplist
	  while (bottom.down != null){
		  bottom = bottom.down;
	  }
	  last = bottom.left;
	  return (last);
  }

  public SkipListEntry ceilingEntry(String k)
  {
	  SkipListEntry Entry = findEntry(k);
	  if (Entry.key == k)
		  return Entry;
	  else
		  return Entry.right;
  }


  public SkipListEntry floorEntry(String k)
  {
	  SkipListEntry Entry = findEntry(k);
	  if (Entry.key == k)
		  return Entry;
	  else
		  return Entry;
  }


  public SkipListEntry upperEntry(String k)
  {
     SkipListEntry Entry = findEntry(k);
     return Entry.right;
  }


  public SkipListEntry lowerEntry(String k)
  {
     SkipListEntry Entry = findEntry(k);
     
     if (Entry.key == k)
    	 return Entry.left;
     return Entry;
  }



  /* --------------------- Skip list print methods..... ---------------- */

  public void printHorizontal()
  {
     String s = "";
     int i;

     SkipListEntry p;

     /* ----------------------------------
	Record the position of each entry
	---------------------------------- */
     p = head;

     while ( p.down != null )
     {
        p = p.down;
     }

     i = 0;
     while ( p != null )
     {
        p.pos = i++;
        p = p.right;
     }

     /* -------------------
	Print...
	------------------- */
     p = head;

     while ( p != null )
     {
        s = getOneRow( p );
	System.out.println(s);

        p = p.down;
     }
  }

  public String getOneRow( SkipListEntry p )
  {
     String s;
     int a, b, i;

     a = 0;

     s = "" + p.key;
     p = p.right;


     while ( p != null )
     {
        SkipListEntry q;

        q = p;
        while (q.down != null)
	   q = q.down;
        b = q.pos;

        s = s + " <-";


        for (i = a+1; i < b; i++)
           s = s + "--------";
 
        s = s + "> " + p.key;

        a = b;

        p = p.right;
     }

     return(s);
  }

  public void printVertical()
  {
     String s = "";

     SkipListEntry p;

     p = head;

     while ( p.down != null )
        p = p.down;

     while ( p != null )
     {
        s = getOneColumn( p );
	System.out.println(s);

        p = p.right;
     }
  }


  public String getOneColumn( SkipListEntry p )
  {
     String s = "";

     while ( p != null )
     {
        s = s + " " + p.key;

        p = p.up;
     }

     return(s);
  }

} 