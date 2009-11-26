begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|jaxrs
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
name|ArrayList
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|LogManager
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|LogRecord
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Feed
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|endpoint
operator|.
name|Server
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
name|JAXRSServerFactoryBean
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
name|logging
operator|.
name|atom
operator|.
name|AtomPushHandler
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
name|logging
operator|.
name|atom
operator|.
name|converter
operator|.
name|Converter
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
name|logging
operator|.
name|atom
operator|.
name|converter
operator|.
name|StandardConverter
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
name|logging
operator|.
name|atom
operator|.
name|converter
operator|.
name|StandardConverter
operator|.
name|Format
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
name|logging
operator|.
name|atom
operator|.
name|converter
operator|.
name|StandardConverter
operator|.
name|Multiplicity
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
name|logging
operator|.
name|atom
operator|.
name|converter
operator|.
name|StandardConverter
operator|.
name|Output
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
name|logging
operator|.
name|atom
operator|.
name|deliverer
operator|.
name|Deliverer
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
name|logging
operator|.
name|atom
operator|.
name|deliverer
operator|.
name|WebClientDeliverer
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
name|provider
operator|.
name|AtomEntryProvider
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
name|provider
operator|.
name|AtomFeedProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSLoggingAtomPushTest
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JAXRSLoggingAtomPushTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|Server
name|server
decl_stmt|;
specifier|private
specifier|static
name|List
argument_list|<
name|Feed
argument_list|>
name|feeds
init|=
operator|new
name|ArrayList
argument_list|<
name|Feed
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|List
argument_list|<
name|Entry
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<
name|Entry
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Ignore
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
specifier|public
specifier|static
class|class
name|Resource
block|{
annotation|@
name|POST
specifier|public
name|void
name|consume
parameter_list|(
name|Feed
name|feed
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|feed
argument_list|)
expr_stmt|;
name|feeds
operator|.
name|add
argument_list|(
name|feed
argument_list|)
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/atomPub"
argument_list|)
specifier|public
name|void
name|consume
parameter_list|(
name|Entry
name|entry
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|entries
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
throws|throws
name|Exception
block|{
comment|// disable logging for server startup
name|configureLogging
argument_list|(
literal|"resources/logging_atompush_disabled.properties"
argument_list|)
expr_stmt|;
name|JAXRSServerFactoryBean
name|sf
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|JAXRSLoggingAtomPushTest
operator|.
name|Resource
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9080/"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setProviders
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|AtomFeedProvider
argument_list|()
argument_list|,
operator|new
name|AtomEntryProvider
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|server
operator|=
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
comment|/** Configures global logging */
specifier|private
specifier|static
name|void
name|configureLogging
parameter_list|(
name|String
name|propFile
parameter_list|)
throws|throws
name|Exception
block|{
name|LogManager
name|lm
init|=
name|LogManager
operator|.
name|getLogManager
argument_list|()
decl_stmt|;
name|InputStream
name|ins
init|=
name|JAXRSLoggingAtomPushTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|propFile
argument_list|)
decl_stmt|;
name|lm
operator|.
name|readConfiguration
argument_list|(
name|ins
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|logSixEvents
parameter_list|(
name|Logger
name|log
parameter_list|)
block|{
name|log
operator|.
name|severe
argument_list|(
literal|"severe message"
argument_list|)
expr_stmt|;
name|log
operator|.
name|warning
argument_list|(
literal|"warning message"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"info message"
argument_list|)
expr_stmt|;
name|LogRecord
name|r
init|=
operator|new
name|LogRecord
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"fine message"
argument_list|)
decl_stmt|;
name|r
operator|.
name|setThrown
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"tadaam"
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|log
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|r
operator|=
operator|new
name|LogRecord
argument_list|(
name|Level
operator|.
name|FINER
argument_list|,
literal|"finer message with {0} and {1}"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setParameters
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"param1"
block|,
literal|"param2"
block|}
argument_list|)
expr_stmt|;
name|r
operator|.
name|setLoggerName
argument_list|(
literal|"faky-logger"
argument_list|)
expr_stmt|;
name|log
operator|.
name|log
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|log
operator|.
name|finest
argument_list|(
literal|"finest message"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|afterClass
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|LogManager
name|lm
init|=
name|LogManager
operator|.
name|getLogManager
argument_list|()
decl_stmt|;
try|try
block|{
comment|// restoring original configuration to not use tested logging handlers
name|lm
operator|.
name|readConfiguration
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore missing config file
block|}
block|}
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|Exception
block|{
name|feeds
operator|.
name|clear
argument_list|()
expr_stmt|;
name|entries
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOneElementBatch
parameter_list|()
throws|throws
name|Exception
block|{
name|configureLogging
argument_list|(
literal|"resources/logging_atompush.properties"
argument_list|)
expr_stmt|;
name|logSixEvents
argument_list|(
name|LOG
argument_list|)
expr_stmt|;
comment|// need to wait: multithreaded and client-server journey
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Different logged events count;"
argument_list|,
literal|6
argument_list|,
name|feeds
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultiElementBatch
parameter_list|()
throws|throws
name|Exception
block|{
name|configureLogging
argument_list|(
literal|"resources/logging_atompush_batch.properties"
argument_list|)
expr_stmt|;
name|logSixEvents
argument_list|(
name|LOG
argument_list|)
expr_stmt|;
comment|// need to wait: multithreaded and client-server journey
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// 6 events / 3 element batch = 2 feeds expected
name|assertEquals
argument_list|(
literal|"Different logged events count;"
argument_list|,
literal|2
argument_list|,
name|feeds
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrivateLogger
parameter_list|()
throws|throws
name|Exception
block|{
name|configureLogging
argument_list|(
literal|"resources/logging_atompush_disabled.properties"
argument_list|)
expr_stmt|;
name|Logger
name|log
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JAXRSLoggingAtomPushTest
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"private-log"
argument_list|)
decl_stmt|;
name|Converter
name|c
init|=
operator|new
name|StandardConverter
argument_list|(
name|Output
operator|.
name|FEED
argument_list|,
name|Multiplicity
operator|.
name|ONE
argument_list|,
name|Format
operator|.
name|CONTENT
argument_list|)
decl_stmt|;
name|Deliverer
name|d
init|=
operator|new
name|WebClientDeliverer
argument_list|(
literal|"http://localhost:9080"
argument_list|)
decl_stmt|;
name|Handler
name|h
init|=
operator|new
name|AtomPushHandler
argument_list|(
literal|2
argument_list|,
name|c
argument_list|,
name|d
argument_list|)
decl_stmt|;
name|log
operator|.
name|addHandler
argument_list|(
name|h
argument_list|)
expr_stmt|;
name|log
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|ALL
argument_list|)
expr_stmt|;
name|logSixEvents
argument_list|(
name|log
argument_list|)
expr_stmt|;
comment|// need to wait: multithreaded and client-server journey
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// 6 events / 2 element batch = 3 feeds expected
name|assertEquals
argument_list|(
literal|"Different logged events count;"
argument_list|,
literal|3
argument_list|,
name|feeds
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAtomPubEntries
parameter_list|()
throws|throws
name|Exception
block|{
name|configureLogging
argument_list|(
literal|"resources/logging_atompush_atompub.properties"
argument_list|)
expr_stmt|;
name|logSixEvents
argument_list|(
name|LOG
argument_list|)
expr_stmt|;
comment|// need to wait: multithreaded and client-server journey
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// 6 events logged as entries
name|assertEquals
argument_list|(
literal|"Different logged events count;"
argument_list|,
literal|6
argument_list|,
name|entries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

