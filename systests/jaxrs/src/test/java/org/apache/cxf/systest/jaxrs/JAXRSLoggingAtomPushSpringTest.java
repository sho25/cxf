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
name|StringReader
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
name|GET
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
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|Element
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
name|ExtensibleElement
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecords
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
name|testutil
operator|.
name|common
operator|.
name|AbstractClientServerTestBase
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

begin_class
specifier|public
class|class
name|JAXRSLoggingAtomPushSpringTest
extends|extends
name|AbstractClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|SpringServer
operator|.
name|PORT
decl_stmt|;
specifier|private
name|JAXBContext
name|context
decl_stmt|;
specifier|private
name|int
name|fakyLogger
decl_stmt|;
specifier|private
name|int
name|namedLogger
decl_stmt|;
specifier|private
name|int
name|resourceLogger
decl_stmt|;
specifier|private
name|int
name|throwables
decl_stmt|;
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
comment|// must be 'in-process' to communicate with inner class in single JVM
comment|// and to spawn class SpringServer w/o using main() method
name|launchServer
argument_list|(
name|SpringServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|SpringServer
extends|extends
name|AbstractSpringServer
block|{
specifier|public
name|SpringServer
parameter_list|()
block|{
name|super
argument_list|(
literal|"/jaxrs_logging_atompush"
argument_list|)
expr_stmt|;
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
name|Resource
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Resource2
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Resource3
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Resource4
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Resource5
operator|.
name|clear
argument_list|()
expr_stmt|;
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|LogRecords
operator|.
name|class
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecord
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFeedsWithLogRecordsOneEntry
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/root"
argument_list|)
decl_stmt|;
try|try
block|{
name|wc
operator|.
name|path
argument_list|(
literal|"/log"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Feed
argument_list|>
name|elements
init|=
name|Resource
operator|.
name|getElements
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|elements
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|resetCounters
argument_list|()
expr_stmt|;
for|for
control|(
name|Feed
name|feed
range|:
name|elements
control|)
block|{
name|List
argument_list|<
name|Entry
argument_list|>
name|entries
init|=
name|feed
operator|.
name|getEntries
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|entries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Entry
name|e
init|=
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|LogRecords
name|records
init|=
name|readLogRecords
argument_list|(
name|e
operator|.
name|getContent
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecord
argument_list|>
name|list
init|=
name|records
operator|.
name|getLogRecords
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|updateCounters
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"Resource"
argument_list|)
expr_stmt|;
block|}
name|verifyCounters
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFeedsWithBatchLogRecordsOneEntry
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/batch"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"/log"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Feed
argument_list|>
name|elements
init|=
name|Resource2
operator|.
name|getElements
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|elements
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|resetCounters
argument_list|()
expr_stmt|;
for|for
control|(
name|Feed
name|feed
range|:
name|elements
control|)
block|{
name|List
argument_list|<
name|Entry
argument_list|>
name|entries
init|=
name|feed
operator|.
name|getEntries
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|entries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Entry
name|e
range|:
name|entries
control|)
block|{
name|updateCounters
argument_list|(
name|readLogRecord
argument_list|(
name|e
operator|.
name|getContent
argument_list|()
argument_list|)
argument_list|,
literal|"Resource2"
argument_list|)
expr_stmt|;
block|}
block|}
name|verifyCounters
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEntriesWithLogRecordsOneEntry
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/entries"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"/log"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Entry
argument_list|>
name|elements
init|=
name|Resource3
operator|.
name|getElements
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|elements
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|resetCounters
argument_list|()
expr_stmt|;
for|for
control|(
name|Entry
name|e
range|:
name|elements
control|)
block|{
name|updateCounters
argument_list|(
name|readLogRecord
argument_list|(
name|e
operator|.
name|getContent
argument_list|()
argument_list|)
argument_list|,
literal|"Resource3"
argument_list|)
expr_stmt|;
block|}
name|verifyCounters
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testManyEntries
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/entriesMany"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"/log"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Entry
argument_list|>
name|elements
init|=
name|Resource4
operator|.
name|getElements
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|elements
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|resetCounters
argument_list|()
expr_stmt|;
for|for
control|(
name|Entry
name|e
range|:
name|elements
control|)
block|{
name|LogRecords
name|records
init|=
name|readLogRecords
argument_list|(
name|e
operator|.
name|getContent
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecord
argument_list|>
name|list
init|=
name|records
operator|.
name|getLogRecords
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecord
name|record
range|:
name|list
control|)
block|{
name|updateCounters
argument_list|(
name|record
argument_list|,
literal|"Resource4"
argument_list|)
expr_stmt|;
block|}
block|}
name|verifyCounters
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFeedsWithLogRecordsExtension
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/extensions"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"/log"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Feed
argument_list|>
name|elements
init|=
name|Resource5
operator|.
name|getElements
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|elements
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|resetCounters
argument_list|()
expr_stmt|;
for|for
control|(
name|Feed
name|feed
range|:
name|elements
control|)
block|{
name|List
argument_list|<
name|Entry
argument_list|>
name|entries
init|=
name|feed
operator|.
name|getEntries
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|entries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Entry
name|e
init|=
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|LogRecords
name|records
init|=
name|readLogRecordsExtension
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecord
argument_list|>
name|list
init|=
name|records
operator|.
name|getLogRecords
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|updateCounters
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"Resource5"
argument_list|)
expr_stmt|;
block|}
name|verifyCounters
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Ignore
annotation|@
name|Path
argument_list|(
literal|"/root"
argument_list|)
specifier|public
specifier|static
class|class
name|Resource
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG2
decl_stmt|;
static|static
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|LOG1
operator|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|Resource
operator|.
name|class
argument_list|)
expr_stmt|;
name|LOG2
operator|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|Resource
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"namedLogger"
argument_list|)
expr_stmt|;
block|}
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
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/log"
argument_list|)
specifier|public
name|void
name|doLogging
parameter_list|()
block|{
name|doLog
argument_list|(
name|LOG1
argument_list|,
name|LOG2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/feeds"
argument_list|)
specifier|public
name|void
name|consume
parameter_list|(
name|Feed
name|feed
parameter_list|)
block|{
name|feed
operator|.
name|toString
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|Resource
operator|.
name|class
init|)
block|{
name|feeds
operator|.
name|add
argument_list|(
name|feed
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|clear
parameter_list|()
block|{
name|feeds
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|List
argument_list|<
name|Feed
argument_list|>
name|getElements
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Feed
argument_list|>
argument_list|(
name|feeds
argument_list|)
return|;
block|}
block|}
annotation|@
name|Ignore
annotation|@
name|Path
argument_list|(
literal|"/batch"
argument_list|)
specifier|public
specifier|static
class|class
name|Resource2
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG1
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|Resource2
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG2
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|Resource2
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"namedLogger"
argument_list|)
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
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/log"
argument_list|)
specifier|public
name|void
name|doLogging
parameter_list|()
block|{
name|doLog
argument_list|(
name|LOG1
argument_list|,
name|LOG2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/feeds"
argument_list|)
specifier|public
name|void
name|consume
parameter_list|(
name|Feed
name|feed
parameter_list|)
block|{
name|feed
operator|.
name|toString
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|Resource2
operator|.
name|class
init|)
block|{
name|feeds
operator|.
name|add
argument_list|(
name|feed
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|clear
parameter_list|()
block|{
name|feeds
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|List
argument_list|<
name|Feed
argument_list|>
name|getElements
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Feed
argument_list|>
argument_list|(
name|feeds
argument_list|)
return|;
block|}
block|}
annotation|@
name|Ignore
annotation|@
name|Path
argument_list|(
literal|"/entries"
argument_list|)
specifier|public
specifier|static
class|class
name|Resource3
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG1
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|Resource3
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG2
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|Resource3
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"namedLogger"
argument_list|)
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
name|GET
annotation|@
name|Path
argument_list|(
literal|"/log"
argument_list|)
specifier|public
name|void
name|doLogging
parameter_list|()
block|{
name|doLog
argument_list|(
name|LOG1
argument_list|,
name|LOG2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/entries"
argument_list|)
specifier|public
name|void
name|consume
parameter_list|(
name|Entry
name|entry
parameter_list|)
block|{
name|entry
operator|.
name|toString
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|Resource3
operator|.
name|class
init|)
block|{
name|entries
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|clear
parameter_list|()
block|{
name|entries
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|List
argument_list|<
name|Entry
argument_list|>
name|getElements
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Entry
argument_list|>
argument_list|(
name|entries
argument_list|)
return|;
block|}
block|}
annotation|@
name|Ignore
annotation|@
name|Path
argument_list|(
literal|"/entriesMany"
argument_list|)
specifier|public
specifier|static
class|class
name|Resource4
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG1
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|Resource4
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG2
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|Resource4
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"namedLogger"
argument_list|)
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
name|GET
annotation|@
name|Path
argument_list|(
literal|"/log"
argument_list|)
specifier|public
name|void
name|doLogging
parameter_list|()
block|{
name|doLog
argument_list|(
name|LOG1
argument_list|,
name|LOG2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/entries"
argument_list|)
specifier|public
name|void
name|consume
parameter_list|(
name|Entry
name|entry
parameter_list|)
block|{
name|entry
operator|.
name|toString
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|Resource4
operator|.
name|class
init|)
block|{
name|entries
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|clear
parameter_list|()
block|{
name|entries
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
specifier|synchronized
name|List
argument_list|<
name|Entry
argument_list|>
name|getElements
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Entry
argument_list|>
argument_list|(
name|entries
argument_list|)
return|;
block|}
block|}
annotation|@
name|Ignore
annotation|@
name|Path
argument_list|(
literal|"/extensions"
argument_list|)
specifier|public
specifier|static
class|class
name|Resource5
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG1
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|Resource5
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG2
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|Resource5
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"namedLogger"
argument_list|)
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
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/log"
argument_list|)
specifier|public
name|void
name|doLogging
parameter_list|()
block|{
name|doLog
argument_list|(
name|LOG1
argument_list|,
name|LOG2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/feeds"
argument_list|)
specifier|public
name|void
name|consume
parameter_list|(
name|Feed
name|feed
parameter_list|)
block|{
name|feed
operator|.
name|toString
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|Resource5
operator|.
name|class
init|)
block|{
name|feeds
operator|.
name|add
argument_list|(
name|feed
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|clear
parameter_list|()
block|{
name|feeds
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Feed
argument_list|>
name|getElements
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Feed
argument_list|>
argument_list|(
name|feeds
argument_list|)
return|;
block|}
block|}
specifier|private
specifier|static
name|void
name|doLog
parameter_list|(
name|Logger
name|l1
parameter_list|,
name|Logger
name|l2
parameter_list|)
block|{
name|l1
operator|.
name|severe
argument_list|(
literal|"severe message"
argument_list|)
expr_stmt|;
name|l1
operator|.
name|warning
argument_list|(
literal|"warning message"
argument_list|)
expr_stmt|;
name|l1
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
name|l1
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
name|l1
operator|.
name|log
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|l1
operator|.
name|finest
argument_list|(
literal|"finest message"
argument_list|)
expr_stmt|;
comment|// for LOG2 only 'warning' and above messages should be logged
name|l2
operator|.
name|severe
argument_list|(
literal|"severe message"
argument_list|)
expr_stmt|;
name|l2
operator|.
name|severe
argument_list|(
literal|"severe message2"
argument_list|)
expr_stmt|;
name|l2
operator|.
name|info
argument_list|(
literal|"info message - should not pass!"
argument_list|)
expr_stmt|;
name|l2
operator|.
name|finer
argument_list|(
literal|"finer message - should not pass!"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|LogRecords
name|readLogRecords
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|(
name|LogRecords
operator|)
name|context
operator|.
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
operator|new
name|StringReader
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecord
name|readLogRecord
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecord
operator|)
name|context
operator|.
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
operator|new
name|StringReader
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|LogRecords
name|readLogRecordsExtension
parameter_list|(
name|Entry
name|e
parameter_list|)
throws|throws
name|Exception
block|{
name|ExtensibleElement
name|el
init|=
name|e
operator|.
name|getExtension
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/log"
argument_list|,
literal|"logRecords"
argument_list|,
literal|"log"
argument_list|)
argument_list|)
decl_stmt|;
name|LogRecords
name|records
init|=
operator|new
name|LogRecords
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecord
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecord
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|element
range|:
name|el
operator|.
name|getElements
argument_list|()
control|)
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecord
name|record
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecord
argument_list|()
decl_stmt|;
name|Element
name|loggerName
init|=
name|element
operator|.
name|getFirstChild
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/log"
argument_list|,
literal|"loggerName"
argument_list|,
literal|"log"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|loggerName
operator|!=
literal|null
condition|)
block|{
name|record
operator|.
name|setLoggerName
argument_list|(
name|loggerName
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Element
name|throwable
init|=
name|element
operator|.
name|getFirstChild
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/log"
argument_list|,
literal|"throwable"
argument_list|,
literal|"log"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|throwable
operator|!=
literal|null
condition|)
block|{
name|record
operator|.
name|setThrowable
argument_list|(
name|throwable
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|add
argument_list|(
name|record
argument_list|)
expr_stmt|;
block|}
name|records
operator|.
name|setLogRecords
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|records
return|;
block|}
specifier|private
name|void
name|updateCounters
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|LogRecord
name|record
parameter_list|,
name|String
name|clsName
parameter_list|)
block|{
name|String
name|name
init|=
name|record
operator|.
name|getLoggerName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
name|name
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
operator|(
literal|"org.apache.cxf.systest.jaxrs.JAXRSLoggingAtomPushSpringTest$"
operator|+
name|clsName
operator|)
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|resourceLogger
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"namedLogger"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|namedLogger
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"faky-logger"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|fakyLogger
operator|++
expr_stmt|;
block|}
block|}
else|else
block|{
name|assertNotNull
argument_list|(
name|record
operator|.
name|getThrowable
argument_list|()
argument_list|)
expr_stmt|;
name|throwables
operator|++
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|resetCounters
parameter_list|()
block|{
name|fakyLogger
operator|=
literal|0
expr_stmt|;
name|namedLogger
operator|=
literal|0
expr_stmt|;
name|resourceLogger
operator|=
literal|0
expr_stmt|;
name|throwables
operator|=
literal|0
expr_stmt|;
block|}
specifier|private
name|void
name|verifyCounters
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|throwables
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|resourceLogger
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|namedLogger
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|fakyLogger
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

