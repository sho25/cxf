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
operator|.
name|simple
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Queue
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|Oneway
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|Attachment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|Multipart
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|tika
operator|.
name|TikaContentExtractor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|tika
operator|.
name|TikaContentExtractor
operator|.
name|TikaContent
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
name|JavaRDD
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
name|JavaStreamingContext
import|;
end_import

begin_import
import|import
name|demo
operator|.
name|jaxrs
operator|.
name|server
operator|.
name|SparkUtils
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
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|MediaType
argument_list|>
name|MEDIA_TYPE_TABLE
decl_stmt|;
static|static
block|{
name|MEDIA_TYPE_TABLE
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|MediaType
argument_list|>
argument_list|()
expr_stmt|;
name|MEDIA_TYPE_TABLE
operator|.
name|put
argument_list|(
literal|"pdf"
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"application/pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|MEDIA_TYPE_TABLE
operator|.
name|put
argument_list|(
literal|"odt"
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"application/vnd.oasis.opendocument.text"
argument_list|)
argument_list|)
expr_stmt|;
name|MEDIA_TYPE_TABLE
operator|.
name|put
argument_list|(
literal|"odp"
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"application/vnd.oasis.opendocument.presentation"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|String
name|receiverType
decl_stmt|;
specifier|public
name|StreamingService
parameter_list|(
name|String
name|receiverType
parameter_list|)
block|{
name|this
operator|.
name|receiverType
operator|=
name|receiverType
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/multipart"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|void
name|processMultipartStream
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|async
parameter_list|,
annotation|@
name|Multipart
argument_list|(
literal|"file"
argument_list|)
name|Attachment
name|att
parameter_list|)
block|{
name|MediaType
name|mediaType
init|=
name|att
operator|.
name|getContentType
argument_list|()
decl_stmt|;
if|if
condition|(
name|mediaType
operator|==
literal|null
condition|)
block|{
name|String
name|fileName
init|=
name|att
operator|.
name|getContentDisposition
argument_list|()
operator|.
name|getFilename
argument_list|()
decl_stmt|;
if|if
condition|(
name|fileName
operator|!=
literal|null
condition|)
block|{
name|int
name|extDot
init|=
name|fileName
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|extDot
operator|>
literal|0
condition|)
block|{
name|mediaType
operator|=
name|MEDIA_TYPE_TABLE
operator|.
name|get
argument_list|(
name|fileName
operator|.
name|substring
argument_list|(
name|extDot
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|TikaContentExtractor
name|tika
init|=
operator|new
name|TikaContentExtractor
argument_list|()
decl_stmt|;
name|TikaContent
name|tikaContent
init|=
name|tika
operator|.
name|extract
argument_list|(
name|att
operator|.
name|getObject
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|,
name|mediaType
argument_list|)
decl_stmt|;
name|processStream
argument_list|(
name|async
argument_list|,
name|SparkUtils
operator|.
name|getStringsFromString
argument_list|(
name|tikaContent
operator|.
name|getContent
argument_list|()
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
name|processSimpleStream
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
name|processStream
argument_list|(
name|async
argument_list|,
name|SparkUtils
operator|.
name|getStringsFromInputStream
argument_list|(
name|is
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/streamOneWay"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Oneway
specifier|public
name|void
name|processSimpleStreamOneWay
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|processStreamOneWay
argument_list|(
name|SparkUtils
operator|.
name|getStringsFromInputStream
argument_list|(
name|is
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processStream
parameter_list|(
name|AsyncResponse
name|async
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|inputStrings
parameter_list|)
block|{
try|try
block|{
name|SparkConf
name|sparkConf
init|=
operator|new
name|SparkConf
argument_list|()
operator|.
name|setMaster
argument_list|(
literal|"local[*]"
argument_list|)
operator|.
name|setAppName
argument_list|(
literal|"JAX-RS Spark Connect "
operator|+
name|SparkUtils
operator|.
name|getRandomId
argument_list|()
argument_list|)
decl_stmt|;
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
name|JavaDStream
argument_list|<
name|String
argument_list|>
name|receiverStream
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"queue"
operator|.
name|equals
argument_list|(
name|receiverType
argument_list|)
condition|)
block|{
name|Queue
argument_list|<
name|JavaRDD
argument_list|<
name|String
argument_list|>
argument_list|>
name|rddQueue
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|30
condition|;
name|i
operator|++
control|)
block|{
name|rddQueue
operator|.
name|add
argument_list|(
name|jssc
operator|.
name|sparkContext
argument_list|()
operator|.
name|parallelize
argument_list|(
name|inputStrings
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|receiverStream
operator|=
name|jssc
operator|.
name|queueStream
argument_list|(
name|rddQueue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|receiverStream
operator|=
name|jssc
operator|.
name|receiverStream
argument_list|(
operator|new
name|StringListReceiver
argument_list|(
name|inputStrings
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|JavaPairDStream
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|wordCounts
init|=
name|SparkUtils
operator|.
name|createOutputDStream
argument_list|(
name|receiverStream
argument_list|,
literal|false
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
specifier|private
name|void
name|processStreamOneWay
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|inputStrings
parameter_list|)
block|{
try|try
block|{
name|SparkConf
name|sparkConf
init|=
operator|new
name|SparkConf
argument_list|()
operator|.
name|setMaster
argument_list|(
literal|"local[*]"
argument_list|)
operator|.
name|setAppName
argument_list|(
literal|"JAX-RS Spark Connect OneWay "
operator|+
name|SparkUtils
operator|.
name|getRandomId
argument_list|()
argument_list|)
decl_stmt|;
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
name|JavaDStream
argument_list|<
name|String
argument_list|>
name|receiverStream
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"queue"
operator|.
name|equals
argument_list|(
name|receiverType
argument_list|)
condition|)
block|{
name|Queue
argument_list|<
name|JavaRDD
argument_list|<
name|String
argument_list|>
argument_list|>
name|rddQueue
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|30
condition|;
name|i
operator|++
control|)
block|{
name|rddQueue
operator|.
name|add
argument_list|(
name|jssc
operator|.
name|sparkContext
argument_list|()
operator|.
name|parallelize
argument_list|(
name|inputStrings
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|receiverStream
operator|=
name|jssc
operator|.
name|queueStream
argument_list|(
name|rddQueue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|receiverStream
operator|=
name|jssc
operator|.
name|receiverStream
argument_list|(
operator|new
name|StringListReceiver
argument_list|(
name|inputStrings
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|JavaPairDStream
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|wordCounts
init|=
name|SparkUtils
operator|.
name|createOutputDStream
argument_list|(
name|receiverStream
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|wordCounts
operator|.
name|foreachRDD
argument_list|(
operator|new
name|PrintOutputFunction
argument_list|(
name|jssc
argument_list|)
argument_list|)
expr_stmt|;
name|jssc
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
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
literal|"\n"
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
specifier|private
specifier|static
class|class
name|PrintOutputFunction
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
name|JavaStreamingContext
name|jssc
decl_stmt|;
name|PrintOutputFunction
parameter_list|(
name|JavaStreamingContext
name|jssc
parameter_list|)
block|{
name|this
operator|.
name|jssc
operator|=
name|jssc
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
if|if
condition|(
operator|!
name|rdd
operator|.
name|collectAsMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
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
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|jssc
operator|.
name|stop
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|jssc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

