package com.collection;

/**
 * 自己实现一个hashmap
 * @author Administrator
 *
 */
public class MyHashMap {

	HashMapNode[] table;//位桶
	
	int size;//大小
	
	public MyHashMap(){
		table = new HashMapNode[16];
	}

	/**
	 * 返回hash值
	 * @param v
	 * @param length
	 * @return
	 */
	public int getHash(int v,int length){
		
//		System.out.println("hasd1:" + (v&(length-1)));
//		System.out.println("hash2:" + (v%(length-1)));
		
		return v&(length-1);
	}
	
	public void put(Object key,Object value){
		
		HashMapNode hashMapNode = new HashMapNode();
		hashMapNode.hash = getHash(key.hashCode(), table.length);
		hashMapNode.key = key;
		hashMapNode.value = value;
		hashMapNode.next = null;
		
		HashMapNode lastNode = new HashMapNode();
		boolean isNodeRepeat = false;
		HashMapNode mapNode = table[hashMapNode.hash];
		if(mapNode == null){
			
			table[hashMapNode.hash] = hashMapNode;
			
		}else{
			
			while(mapNode!=null){
				
				if(mapNode.key.equals(key)){

					isNodeRepeat = true;
					mapNode.value = value;
					break;
					
				}else{
					
					lastNode = mapNode;
					mapNode = mapNode.next;
					
				}
			}
			
			//没有发生key重复的情况，则添加到链表最后
			if(!isNodeRepeat){
				lastNode.next = hashMapNode;
			}
			
		}
	}
	
	public static void main(String[] args) {
		
		MyHashMap myHashMap = new MyHashMap();
		myHashMap.put(10, "aa");
		myHashMap.put(20, "bb");
		myHashMap.put(30, "cc");
		myHashMap.put(30, "ssss");
		
		myHashMap.put(53, "gg");
		myHashMap.put(69, "hh");
		myHashMap.put(85, "kk");
		
		System.out.println(myHashMap);
	}
	
}