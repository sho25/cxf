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
name|common
operator|.
name|logging
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|BundleUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IArgumentMatcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|LogUtilsTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetL7dLog
parameter_list|()
throws|throws
name|Exception
block|{
name|Logger
name|log
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|LogUtilsTest
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"testGetL7dLog"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected non-null logger"
argument_list|,
name|log
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected resource bundle name"
argument_list|,
name|BundleUtils
operator|.
name|getBundleName
argument_list|(
name|LogUtilsTest
operator|.
name|class
argument_list|)
argument_list|,
name|log
operator|.
name|getResourceBundleName
argument_list|()
argument_list|)
expr_stmt|;
name|Logger
name|otherLogger
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|LogUtilsTest
operator|.
name|class
argument_list|,
literal|"Messages"
argument_list|,
literal|"testGetL7dLog"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected resource bundle name"
argument_list|,
name|BundleUtils
operator|.
name|getBundleName
argument_list|(
name|LogUtilsTest
operator|.
name|class
argument_list|,
literal|"Messages"
argument_list|)
argument_list|,
name|otherLogger
operator|.
name|getResourceBundleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandleL7dMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|Logger
name|log
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|LogUtilsTest
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"testHandleL7dMessage"
argument_list|)
decl_stmt|;
name|Handler
name|handler
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|Handler
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
comment|// handler called *before* localization of message
name|LogRecord
name|record
init|=
operator|new
name|LogRecord
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"FOOBAR_MSG"
argument_list|)
decl_stmt|;
name|record
operator|.
name|setResourceBundle
argument_list|(
name|log
operator|.
name|getResourceBundle
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|reportMatcher
argument_list|(
operator|new
name|LogRecordMatcher
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
name|handler
operator|.
name|publish
argument_list|(
name|record
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|log
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"FOOBAR_MSG"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|log
operator|.
name|removeHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLogNoParamsOrThrowable
parameter_list|()
block|{
name|Logger
name|log
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|LogUtilsTest
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"testLogNoParamsOrThrowable"
argument_list|)
decl_stmt|;
name|Handler
name|handler
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|Handler
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
comment|// handler called *after* localization of message
name|LogRecord
name|record
init|=
operator|new
name|LogRecord
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"subbed in {0} only"
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|reportMatcher
argument_list|(
operator|new
name|LogRecordMatcher
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
name|handler
operator|.
name|publish
argument_list|(
name|record
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|LogUtils
operator|.
name|log
argument_list|(
name|log
argument_list|,
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"SUB1_MSG"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|log
operator|.
name|removeHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLogNoParamsWithThrowable
parameter_list|()
block|{
name|Logger
name|log
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|LogUtilsTest
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"testLogNoParamsWithThrowable"
argument_list|)
decl_stmt|;
name|Handler
name|handler
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|Handler
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exception
name|ex
init|=
operator|new
name|Exception
argument_list|(
literal|"x"
argument_list|)
decl_stmt|;
name|LogRecord
name|record
init|=
operator|new
name|LogRecord
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"subbed in {0} only"
argument_list|)
decl_stmt|;
name|record
operator|.
name|setThrown
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|reportMatcher
argument_list|(
operator|new
name|LogRecordMatcher
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
name|handler
operator|.
name|publish
argument_list|(
name|record
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|handler
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|log
init|)
block|{
name|log
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
comment|// handler called *after* localization of message
name|LogUtils
operator|.
name|log
argument_list|(
name|log
argument_list|,
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"SUB1_MSG"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|log
operator|.
name|removeHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLogParamSubstitutionWithThrowable
parameter_list|()
throws|throws
name|Exception
block|{
name|Logger
name|log
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|LogUtilsTest
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"testLogParamSubstitutionWithThrowable"
argument_list|)
decl_stmt|;
name|Handler
name|handler
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|Handler
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exception
name|ex
init|=
operator|new
name|Exception
argument_list|()
decl_stmt|;
name|LogRecord
name|record
init|=
operator|new
name|LogRecord
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"subbed in 1 only"
argument_list|)
decl_stmt|;
name|record
operator|.
name|setThrown
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|reportMatcher
argument_list|(
operator|new
name|LogRecordMatcher
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
name|handler
operator|.
name|publish
argument_list|(
name|record
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|handler
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|log
init|)
block|{
name|log
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|LogUtils
operator|.
name|log
argument_list|(
name|log
argument_list|,
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"SUB1_MSG"
argument_list|,
name|ex
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|log
operator|.
name|removeHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLogParamsSubstitutionWithThrowable
parameter_list|()
throws|throws
name|Exception
block|{
name|Logger
name|log
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|LogUtilsTest
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"testLogParamsSubstitutionWithThrowable"
argument_list|)
decl_stmt|;
name|Handler
name|handler
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|Handler
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exception
name|ex
init|=
operator|new
name|Exception
argument_list|()
decl_stmt|;
name|LogRecord
name|record
init|=
operator|new
name|LogRecord
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"subbed in 4& 3"
argument_list|)
decl_stmt|;
name|record
operator|.
name|setThrown
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|reportMatcher
argument_list|(
operator|new
name|LogRecordMatcher
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
name|handler
operator|.
name|publish
argument_list|(
name|record
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|handler
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|log
init|)
block|{
name|log
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|LogUtils
operator|.
name|log
argument_list|(
name|log
argument_list|,
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"SUB2_MSG"
argument_list|,
name|ex
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|3
block|,
literal|4
block|}
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|log
operator|.
name|removeHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXF1420
parameter_list|()
throws|throws
name|Exception
block|{
name|Logger
name|log
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|LogUtilsTest
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"testCXF1420"
argument_list|)
decl_stmt|;
name|LogUtils
operator|.
name|log
argument_list|(
name|log
argument_list|,
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"SQLException for SQL [{call FOO.ping(?, ?)}]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClassMethodNames
parameter_list|()
throws|throws
name|Exception
block|{
name|Logger
name|log
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|LogUtilsTest
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|"testClassMethodNames"
argument_list|)
decl_stmt|;
name|TestLogHandler
name|handler
init|=
operator|new
name|TestLogHandler
argument_list|()
decl_stmt|;
name|log
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
comment|// logger called directly
name|log
operator|.
name|warning
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|String
name|cname
init|=
name|handler
operator|.
name|cname
decl_stmt|;
name|String
name|mname
init|=
name|handler
operator|.
name|mname
decl_stmt|;
comment|// logger called through LogUtils
name|LogUtils
operator|.
name|log
argument_list|(
name|log
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
literal|"FOOBAR_MSG"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|cname
argument_list|,
name|handler
operator|.
name|cname
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mname
argument_list|,
name|handler
operator|.
name|mname
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
specifier|final
class|class
name|TestLogHandler
extends|extends
name|Handler
block|{
name|String
name|cname
decl_stmt|;
name|String
name|mname
decl_stmt|;
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|SecurityException
block|{         }
specifier|public
name|void
name|flush
parameter_list|()
block|{         }
specifier|public
name|void
name|publish
parameter_list|(
name|LogRecord
name|record
parameter_list|)
block|{
name|cname
operator|=
name|record
operator|.
name|getSourceClassName
argument_list|()
expr_stmt|;
name|mname
operator|=
name|record
operator|.
name|getSourceMethodName
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
specifier|final
class|class
name|LogRecordMatcher
implements|implements
name|IArgumentMatcher
block|{
specifier|private
specifier|final
name|LogRecord
name|record
decl_stmt|;
specifier|private
name|LogRecordMatcher
parameter_list|(
name|LogRecord
name|r
parameter_list|)
block|{
name|this
operator|.
name|record
operator|=
name|r
expr_stmt|;
block|}
specifier|public
name|boolean
name|matches
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|LogRecord
condition|)
block|{
name|LogRecord
name|other
init|=
operator|(
name|LogRecord
operator|)
name|obj
decl_stmt|;
name|String
name|l7dString
init|=
literal|"NOT-L7D"
decl_stmt|;
if|if
condition|(
name|record
operator|.
name|getResourceBundle
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|l7dString
operator|=
name|record
operator|.
name|getResourceBundle
argument_list|()
operator|.
name|getString
argument_list|(
name|record
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|record
operator|.
name|getMessage
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|||
name|l7dString
operator|.
name|equals
argument_list|(
name|other
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|)
operator|&&
name|record
operator|.
name|getLevel
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|getLevel
argument_list|()
argument_list|)
operator|&&
name|record
operator|.
name|getThrown
argument_list|()
operator|==
name|other
operator|.
name|getThrown
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|appendTo
parameter_list|(
name|StringBuffer
name|buffer
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"log records did not match"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

