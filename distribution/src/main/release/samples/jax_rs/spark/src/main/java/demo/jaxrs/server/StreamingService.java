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
name|core
operator|.
name|Response
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
name|core
operator|.
name|StreamingOutput
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
name|SparkConf
name|sparkConf
decl_stmt|;
specifier|public
name|StreamingService
parameter_list|(
name|SparkConf
name|sparkConf
parameter_list|)
block|{
name|this
operator|.
name|sparkConf
operator|=
name|sparkConf
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
name|StreamingOutput
name|getStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
try|try
block|{
name|JavaStreamingContext
name|jssc
init|=
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
decl_stmt|;
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
return|return
operator|new
name|SparkStreamingOutput
argument_list|(
name|jssc
argument_list|,
name|createOutputDStream
argument_list|(
name|receiverStream
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|instanceof
name|SparkException
condition|)
block|{
comment|// org.apache.spark.SparkException: Only one SparkContext may be running in this JVM (see SPARK-2243).
comment|// To ignore this error, set spark.driver.allowMultipleContexts = true
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|Response
operator|.
name|status
argument_list|(
literal|503
argument_list|)
operator|.
name|header
argument_list|(
literal|"Retry-After"
argument_list|,
literal|"60"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|ex
argument_list|)
throw|;
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
name|Iterable
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
comment|//new MyReceiverInputDStream(jssc.ssc(),
comment|//                           scala.reflect.ClassTag$.MODULE$.apply(String.class));
comment|//    public static class MyReceiverInputDStream extends ReceiverInputDStream<String> {
comment|//
comment|//        public MyReceiverInputDStream(StreamingContext ssc_, ClassTag<String> evidence$1) {
comment|//            super(ssc_, evidence$1);
comment|//        }
comment|//
comment|//        @Override
comment|//        public Receiver<String> getReceiver() {
comment|//            return new InputStreamReceiver(is);
comment|//        }
comment|//
comment|//    }
block|}
end_class

end_unit

