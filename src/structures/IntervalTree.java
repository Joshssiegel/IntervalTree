package structures;

import java.util.ArrayList;

/**
 * Encapsulates an interval tree.
 * 
 * @author runb-cs112
 */
public class IntervalTree {

	/**
	 * The root of the interval tree
	 */
	IntervalTreeNode root;

	/**
	 * Constructs entire interval tree from set of input intervals. Constructing the tree
	 * means building the interval tree structure and mapping the intervals to the nodes.
	 * 
	 * @param intervals Array list of intervals for which the tree is constructed
	 */
	public IntervalTree(ArrayList<Interval> intervals) {

		// make a copy of intervals to use for right sorting
		ArrayList<Interval> intervalsRight = new ArrayList<Interval>(intervals.size());
		for (Interval iv : intervals) {
			intervalsRight.add(iv);
		}

		// rename input intervals for left sorting
		ArrayList<Interval> intervalsLeft = intervals;

		// sort intervals on left and right end points
		sortIntervals(intervalsLeft, 'l');
		//Delete this:
		System.out.println("SORTED Left:");

		for(Interval I:intervalsLeft)
		{
			System.out.println(I);
		}

		sortIntervals(intervalsRight,'r');

		System.out.println("SORTED Right:");

		for(Interval I:intervalsRight)
		{
			System.out.println(I);
		}
		// get sorted list of end points without duplicates
		ArrayList<Integer> sortedEndPoints = getSortedEndPoints(intervalsLeft, intervalsRight);

		System.out.println("ENDPOINTS: ");

		for(Integer I:sortedEndPoints)
		{
			System.out.println(I);
		}


		// build the tree nodes
		root = buildTreeNodes(sortedEndPoints);
		// map intervals to the tree nodes
		mapIntervalsToTree(intervalsLeft, intervalsRight);

		System.out.println("IN order split values and their intervals: ");
		printTree(root);


	}

	private void printTree(IntervalTreeNode root)
	{
		if(root==null) 
			return;

		printTree(root.leftChild);
		System.out.println("Split: "+root.splitValue+" ");
		System.out.print("Left Intervals: ");
		if(root.leftIntervals!=null)
			for(Interval I:root.leftIntervals)
				System.out.println(I+", ");
		System.out.println();
		System.out.print("Right Intervals: ");
		if(root.rightIntervals!=null)
			for(Interval I:root.rightIntervals)
				System.out.println(I+", ");
		printTree(root.rightChild);
		System.out.println();
	}

	/**
	 * Returns the root of this interval tree.
	 * 
	 * @return Root of interval tree.
	 */
	public IntervalTreeNode getRoot() {
		return root;
	}

	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) {
		// COMPLETE THIS METHOD
		ArrayList<Interval> sorted;
		if(lr=='l')//Sort left endpoints
		{
			sorted=mergeSort(intervals, true);


		}
		else //Sort Right Endpoints
		{
			sorted=mergeSort(intervals, false);

		}
		intervals.clear();
		for(Interval i:sorted)
		{
			intervals.add(i);
		}

	}    


	private static ArrayList<Interval> mergeSort(ArrayList<Interval> array, boolean isSortingLeft)
	{
		if(array.size()==0)
			return array;
		if(array.size()==1)
			return array;
		int leftSize=array.size()/2;
		int rightSize=array.size()-leftSize;
		ArrayList<Interval> left=new ArrayList<Interval>(leftSize);
		ArrayList<Interval> right=new ArrayList<Interval>(rightSize);
		for(int i=0;i<array.size();i++)
		{
			if(i<leftSize)
				left.add(array.get(i));
			else
			{
				//System.out.println("I: "+i+" and length: "+right.length);
				//right[array.length-i-1]
				right.add(array.get(i));
			}
		}
		left=mergeSort(left, isSortingLeft);
		right=mergeSort(right, isSortingLeft);
		if(isSortingLeft)
			array=mergeLeft(left, right);
		else
			array=mergeRight(left, right);


		return array;



	}

	private static ArrayList<Interval> mergeLeft(ArrayList<Interval> left, ArrayList<Interval> right)
	{
		int sortLength=left.size()+right.size();
		int leftLength=left.size();
		int rightLength=right.size();
		ArrayList<Interval> sorted=new ArrayList<Interval>(sortLength);

		int i=0,j=0,k=0;

		while(k<sortLength&&i<leftLength&&j<rightLength)
		{
			if(left.get(i).leftEndPoint>right.get(j).leftEndPoint)
			{
				sorted.add(right.get(j));
				k++;
				j++;
			}
			else
			{
				sorted.add(left.get(i));
				k++;
				i++;				
			}
		}
		while(j<rightLength)
		{
			sorted.add(right.get(j));
			k++;
			j++;
		}
		while(i<leftLength)
		{
			sorted.add(left.get(i));
			k++;
			i++;
		}
		return sorted;

	}

	private static ArrayList<Interval> mergeRight(ArrayList<Interval> left, ArrayList<Interval> right)
	{
		System.out.println("Merged right");
		int sortLength=left.size()+right.size();
		int leftLength=left.size();
		int rightLength=right.size();
		ArrayList<Interval> sorted=new ArrayList<Interval>(sortLength);

		int i=0,j=0,k=0;

		while(k<sortLength&&i<leftLength&&j<rightLength)
		{
			if(left.get(i).rightEndPoint>right.get(j).rightEndPoint)
			{
				sorted.add(right.get(j));
				k++;
				j++;
			}
			else
			{
				sorted.add(left.get(i));
				k++;
				i++;				
			}
		}
		while(j<rightLength)
		{
			sorted.add(right.get(j));
			k++;
			j++;
		}
		while(i<leftLength)
		{
			sorted.add(left.get(i));
			k++;
			i++;
		}
		return sorted;

	}


	/**
	 * Given a set of intervals (left sorted and right sorted), extracts the left and right end points,
	 * and returns a sorted list of the combined end points without duplicates.
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 * @return Sorted array list of all endpoints without duplicates
	 */
	public static ArrayList<Integer> getSortedEndPoints(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		// COMPLETE THIS METHOD
		ArrayList<Integer> points=new ArrayList<Integer>();
		int leftPointer=0, rightPointer=0;

		while(leftPointer<leftSortedIntervals.size()&&rightPointer<rightSortedIntervals.size())
		{
			System.out.println("Comparing: "+leftSortedIntervals.get(leftPointer).leftEndPoint+" AND "+rightSortedIntervals.get(rightPointer).rightEndPoint);
			if(leftSortedIntervals.get(leftPointer).leftEndPoint==rightSortedIntervals.get(rightPointer).rightEndPoint)
			{
				if(!points.contains(leftSortedIntervals.get(leftPointer).leftEndPoint))
					points.add(leftSortedIntervals.get(leftPointer).leftEndPoint);

				leftPointer++;
				rightPointer++;
			}
			else if(leftSortedIntervals.get(leftPointer).leftEndPoint<rightSortedIntervals.get(rightPointer).rightEndPoint)
			{
				if(!points.contains(leftSortedIntervals.get(leftPointer).leftEndPoint))
					points.add(leftSortedIntervals.get(leftPointer).leftEndPoint);

				leftPointer++;
			}
			else if(leftSortedIntervals.get(leftPointer).leftEndPoint>rightSortedIntervals.get(rightPointer).rightEndPoint)
			{
				if(!points.contains(rightSortedIntervals.get(rightPointer).rightEndPoint))
					points.add(rightSortedIntervals.get(rightPointer).rightEndPoint);

				rightPointer++;
			}



		}
		while(leftPointer<leftSortedIntervals.size())
		{			
			if(!points.contains(leftSortedIntervals.get(leftPointer).leftEndPoint))
				points.add(leftSortedIntervals.get(leftPointer).leftEndPoint);

			leftPointer++;

		}
		while(rightPointer<rightSortedIntervals.size())
		{
			if(!points.contains(rightSortedIntervals.get(rightPointer).rightEndPoint))
				points.add(rightSortedIntervals.get(rightPointer).rightEndPoint);

			rightPointer++;
		}
		return points;
	}

	/**
	 * Builds the interval tree structure given a sorted array list of end points
	 * without duplicates.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) {
		// COMPLETE THIS METHOD
		Queue<IntervalTreeNode> Q=new Queue<IntervalTreeNode>();
		for(Integer point:endPoints)
		{
			/*
			 create a tree T with a single node containing p
              set split value of this node to p
              enqueue T in queue Q
			 */

			IntervalTreeNode p=new IntervalTreeNode(point, point, point);
			//IntervalTree T=new IntervalTree(/* IDK WHAT THIS SHOULD BE*/null);
			//T.root=p;//?
			Q.enqueue(p);
		}

		while(true)
		{
			int s=Q.size;

			/*
			 * if s == 1 then 
              T = dequeue Q
              T is the root of the interval tree
              go to step 7
			 */
			if(s==1)
			{
				IntervalTreeNode T=Q.dequeue();
				return T;

			}
			else
			{
				int temps=s;
				while( temps > 1)
				{
					IntervalTreeNode  T1 = Q.dequeue();
					IntervalTreeNode  T2 = Q.dequeue();
					//		               Let v1 be the MAXIMUM split value of leaf nodes in T1
					float v1=T1.maxSplitValue;
					//		               Let v2 be the MINIMUM split value of leaf nodes in T2
					float v2=T2.minSplitValue;
					//		               Create a new node N containing split value x, where x = (v1+v2)/2
					IntervalTreeNode N=new IntervalTreeNode((v1+v2)/2, T1.minSplitValue, T2.maxSplitValue);
					//		               Create a new tree T with N as root, T1 as left child of N, and T2 as right child of N
					//IntervalTree T=new IntervalTree(null);
					N.leftChild=T1;
					N.rightChild=T2;
					//   T.root=N;
					//		               enqueue N into Q
					Q.enqueue(N);
					//						temps = temps - 2
					temps = temps - 2;
				}
				//if temps == 1 do  dequeue from Q and enqueue back into Q 
				if (temps == 1){

					Q.enqueue(Q.dequeue());
				}
			}

		}//End
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE PROGRAM COMPILE
	}

	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		// COMPLETE THIS METHOD
		IntervalTreeNode tmp=root;

		for( Interval I:leftSortedIntervals)
		{
			tmp=root;
			while(!I.contains(tmp.splitValue)&&tmp!=null)
			{
				if(I.rightEndPoint<tmp.splitValue)
				{
					tmp=tmp.leftChild;
				}
				else// Left EndPoint is greater than the splitvalue
				{
					tmp=tmp.rightChild;
				}
			}
			if(tmp!=null){
				if(tmp.leftIntervals==null)
					tmp.leftIntervals=new ArrayList<Interval>();
				tmp.leftIntervals.add(I);
			}

		}

		for( Interval I:rightSortedIntervals)
		{
			tmp=root;
			while(!I.contains(tmp.splitValue)&&tmp!=null)
			{
				if(I.rightEndPoint<tmp.splitValue)
				{
					tmp=tmp.leftChild;
				}
				else// Left EndPoint is greater than the splitvalue
				{
					tmp=tmp.rightChild;
				}
			}
			if(tmp!=null)
			{
				if(tmp.rightIntervals==null)
					tmp.rightIntervals=new ArrayList<Interval>();
				tmp.rightIntervals.add(I);

			}
		}
	}

	/**
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	public ArrayList<Interval> findIntersectingIntervals(Interval q) {
		// COMPLETE THIS METHOD


		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE PROGRAM COMPILE
		return recursiveIntersectingIntervals(q, root);
	}

	private  ArrayList<Interval> recursiveIntersectingIntervals(Interval q, IntervalTreeNode r)
	{
		ArrayList<Interval> resultList=new ArrayList<Interval>();
		ArrayList<Interval> rightSubList=new ArrayList<Interval>();
		ArrayList<Interval> leftSubList=new ArrayList<Interval>();

		if(r.leftChild==null&&r.rightChild==null)
			return resultList;
		float splitVal=r.splitValue;
		if(q.contains(splitVal))
		{
			if(r.leftIntervals!=null){
				for(Interval I:r.leftIntervals)
				{
					System.out.println("Added "+I);
					resultList.add(I);
				}
			}
			leftSubList=recursiveIntersectingIntervals(q, r.leftChild);
			rightSubList=recursiveIntersectingIntervals(q, r.rightChild);
			for(Interval I:leftSubList)
			{
				if(!resultList.contains(I)){
					resultList.add(I);
					System.out.println("Added "+I);

				}
			}
			for(Interval I:rightSubList)
			{
				if(!resultList.contains(I)){
					resultList.add(I);
					System.out.println("Added "+I);

				}
			}

		}
		else if(splitVal<q.leftEndPoint)
		{
			if(r.rightIntervals!=null)
			{
				int i=r.rightIntervals.size()-1;
				while (i >= 0 && r.rightIntervals.get(i).intersects(q))//the i-th interval in Rlist intersects Iq)
				{	 
					resultList.add(r.rightIntervals.get(i));	// Add the i-th interval to ResultList
					System.out.println("Added "+r.rightIntervals.get(i));

					i = i - 1;

				}
			}
			//  Query Rsub and add the results to ResultList
			rightSubList=recursiveIntersectingIntervals(q, r.rightChild);
			for(Interval I:rightSubList)
			{
				if(!resultList.contains(I))
				{
					resultList.add(I);
					System.out.println("Added "+I);

				}
			}
		}
		else if(splitVal>q.rightEndPoint)
		{
			int i=0;
			if(r.leftIntervals!=null)
			{
				while (i < r.leftIntervals.size()&& r.leftIntervals.get(i).intersects(q))//the i-th interval in Llist intersects Iq)
				{
					resultList.add(r.leftIntervals.get(i));	 //Add the ith interval to ResultList
					System.out.println("Added "+r.leftIntervals.get(i));
					i = i + 1;
				}
			}
			//            Query Lsub and add the results to ResultList
			leftSubList=recursiveIntersectingIntervals(q, r.leftChild);
			for(Interval I:leftSubList)
			{
				if(!resultList.contains(I))
				{
					resultList.add(I);
					System.out.println("Added "+I);

				}
			}
		}
		return resultList;

	}

}

