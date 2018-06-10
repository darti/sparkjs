export class SparkSession {
  
public sparkContext() : SparkContext {}
     

public version() : String {}
     

public sharedState() : SharedState {}
     

public sessionState() : SessionState {}
     

public sqlContext() : SQLContext {}
     

public conf() : RuntimeConfig {}
     

public listenerManager() : ExecutionListenerManager {}
     

public experimental() : ExperimentalMethods {}
     

public udf() : UDFRegistration {}
     

public streams() : StreamingQueryManager {}
     

public newSession() : SparkSession {}
     

public emptyDataFrame() : Dataset {}
     

public emptyDataset(evidence$1 : Encoder<T>) : Dataset<T> {}
     

public createDataFrame(rdd : RDD<A>, evidence$2 : TypeTag<A>) : Dataset {}
     

public createDataFrame(data : Seq<A>, evidence$3 : TypeTag<A>) : Dataset {}
     

public createDataFrame(rowRDD : RDD<Row>, schema : StructType) : Dataset {}
     

public createDataFrame(rowRDD : JavaRDD<Row>, schema : StructType) : Dataset {}
     

public createDataFrame(rows : List<Row>, schema : StructType) : Dataset {}
     

public createDataFrame(rdd : RDD<Any>, beanClass : Class<Any>) : Dataset {}
     

public createDataFrame(rdd : JavaRDD<Any>, beanClass : Class<Any>) : Dataset {}
     

public createDataFrame(data : List<Any>, beanClass : Class<Any>) : Dataset {}
     

public baseRelationToDataFrame(baseRelation : BaseRelation) : Dataset {}
     

public createDataset(data : Seq<T>, evidence$4 : Encoder<T>) : Dataset<T> {}
     

public createDataset(data : RDD<T>, evidence$5 : Encoder<T>) : Dataset<T> {}
     

public createDataset(data : List<T>, evidence$6 : Encoder<T>) : Dataset<T> {}
     

public range(end : Long) : Dataset<Long> {}
     

public range(start : Long, end : Long) : Dataset<Long> {}
     

public range(start : Long, end : Long, step : Long) : Dataset<Long> {}
     

public range(start : Long, end : Long, step : Long, numPartitions : Int) : Dataset<Long> {}
     

public catalog() : Catalog {}
     

public table(tableName : String) : Dataset {}
     

public sql(sqlText : String) : Dataset {}
     

public read() : DataFrameReader {}
     

public readStream() : DataStreamReader {}
     

public time(f : <byname><T>) : T {}
     

public implicits() : implicits {}
     

public stop() : Unit {}
     

public close() : Unit {}
     

public internalCreateDataFrame$default$3() : Boolean {}
     
}
     