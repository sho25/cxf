begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ArrayBlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadPoolExecutor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|AsyncResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|Suspended
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|SparkConf
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|SparkException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|api
operator|.
name|java
operator|.
name|JavaPairRDD
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|api
operator|.
name|java
operator|.
name|function
operator|.
name|FlatMapFunction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|api
operator|.
name|java
operator|.
name|function
operator|.
name|Function2
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|api
operator|.
name|java
operator|.
name|function
operator|.
name|PairFunction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|api
operator|.
name|java
operator|.
name|function
operator|.
name|VoidFunction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|Durations
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|api
operator|.
name|java
operator|.
name|JavaDStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|api
operator|.
name|java
operator|.
name|JavaPairDStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|api
operator|.
name|java
operator|.
name|JavaReceiverInputDStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|streaming
operator|.
name|api
operator|.
name|java
operator|.
name|JavaStreamingContext
import|;
end_import

begin_import
import|import
name|scala
operator|.
name|Tuple2
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
specifier|public
class|class
name|StreamingService
block|{
specifier|private
name|Executor
name|executor
init|=
operator|new
name|ThreadPoolExecutor
argument_list|(
literal|5
argument_list|,
literal|5
argument_list|,
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
operator|new
name|ArrayBlockingQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|(
literal|10
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|JavaStreamingContext
name|jssc
decl_stmt|;
specifier|public
name|StreamingService
parameter_list|(
name|SparkConf
name|sparkConf
parameter_list|)
block|{
name|jssc
operator|=
operator|new
name|JavaStreamingContext
argument_list|(
name|sparkConf
argument_list|,
name|Durations
operator|.
name|seconds
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/stream"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|void
name|getStream
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|async
parameter_list|,
name|InputStream
name|is
parameter_list|)
block|{
try|try
block|{
name|SparkStreamingOutput
name|streamOut
init|=
operator|new
name|SparkStreamingOutput
argument_list|(
name|jssc
argument_list|)
decl_stmt|;
name|SparkStreamingListener
name|sparkListener
init|=
operator|new
name|SparkStreamingListener
argument_list|(
name|streamOut
argument_list|)
decl_stmt|;
name|jssc
operator|.
name|addStreamingListener
argument_list|(
name|sparkListener
argument_list|)
expr_stmt|;
name|JavaReceiverInputDStream
argument_list|<
name|String
argument_list|>
name|receiverStream
init|=
name|jssc
operator|.
name|receiverStream
argument_list|(
operator|new
name|InputStreamReceiver
argument_list|(
name|is
argument_list|)
argument_list|)
decl_stmt|;
name|JavaPairDStream
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|wordCounts
init|=
name|createOutputDStream
argument_list|(
name|receiverStream
argument_list|)
decl_stmt|;
name|wordCounts
operator|.
name|foreachRDD
argument_list|(
operator|new
name|OutputFunction
argument_list|(
name|streamOut
argument_list|)
argument_list|)
expr_stmt|;
name|jssc
operator|.
name|start
argument_list|()
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|SparkJob
argument_list|(
name|async
argument_list|,
name|sparkListener
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// the compiler does not allow to catch SparkException directly
if|if
condition|(
name|ex
operator|instanceof
name|SparkException
condition|)
block|{
name|async
operator|.
name|cancel
argument_list|(
literal|60
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|async
operator|.
name|resume
argument_list|(
operator|new
name|WebApplicationException
argument_list|(
name|ex
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
specifier|private
specifier|static
name|JavaPairDStream
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|createOutputDStream
parameter_list|(
name|JavaReceiverInputDStream
argument_list|<
name|String
argument_list|>
name|receiverStream
parameter_list|)
block|{
specifier|final
name|JavaDStream
argument_list|<
name|String
argument_list|>
name|words
init|=
name|receiverStream
operator|.
name|flatMap
argument_list|(
operator|new
name|FlatMapFunction
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|call
parameter_list|(
name|String
name|x
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|x
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
specifier|final
name|JavaPairDStream
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|pairs
init|=
name|words
operator|.
name|mapToPair
argument_list|(
operator|new
name|PairFunction
argument_list|<
name|String
argument_list|,
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Tuple2
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|call
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
operator|new
name|Tuple2
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|s
argument_list|,
literal|1
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|pairs
operator|.
name|reduceByKey
argument_list|(
operator|new
name|Function2
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|call
parameter_list|(
name|Integer
name|i1
parameter_list|,
name|Integer
name|i2
parameter_list|)
block|{
return|return
name|i1
operator|+
name|i2
return|;
block|}
block|}
argument_list|)
return|;
block|}
specifier|private
specifier|static
class|class
name|OutputFunction
implements|implements
name|VoidFunction
argument_list|<
name|JavaPairRDD
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|private
name|SparkStreamingOutput
name|streamOut
decl_stmt|;
name|OutputFunction
parameter_list|(
name|SparkStreamingOutput
name|streamOut
parameter_list|)
block|{
name|this
operator|.
name|streamOut
operator|=
name|streamOut
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|call
parameter_list|(
name|JavaPairRDD
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|rdd
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|rdd
operator|.
name|collectAsMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" : "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
operator|+
literal|"\r\n"
decl_stmt|;
name|streamOut
operator|.
name|addResponseEntry
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

