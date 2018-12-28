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
name|jca
operator|.
name|core
operator|.
name|resourceadapter
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
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|Before
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|ResourceAdapterInternalExceptionTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|EXCEPTION_LOGGER
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|ResourceAdapterInternalException
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Level
name|logLevel
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|logLevel
operator|=
name|EXCEPTION_LOGGER
operator|.
name|getLevel
argument_list|()
expr_stmt|;
name|EXCEPTION_LOGGER
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|EXCEPTION_LOGGER
operator|.
name|setLevel
argument_list|(
name|logLevel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMessage
parameter_list|()
block|{
specifier|final
name|String
name|msg
init|=
literal|"msg1"
operator|.
name|intern
argument_list|()
decl_stmt|;
name|Exception
name|e
init|=
operator|new
name|ResourceAdapterInternalException
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|msg
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"reason"
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMessageWithNullTx
parameter_list|()
block|{
specifier|final
name|String
name|msg
init|=
literal|"msg1"
operator|.
name|intern
argument_list|()
decl_stmt|;
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ResourceAdapterInternalException
name|e
init|=
operator|new
name|ResourceAdapterInternalException
argument_list|(
name|msg
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|msg
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"reason"
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMessageWithEx
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|msg
init|=
literal|"msg"
decl_stmt|;
specifier|final
name|String
name|causeMsg
init|=
literal|"cause"
decl_stmt|;
name|Exception
name|cause
init|=
operator|new
name|RuntimeException
argument_list|(
name|causeMsg
argument_list|)
decl_stmt|;
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ResourceAdapterInternalException
name|e
init|=
operator|new
name|ResourceAdapterInternalException
argument_list|(
name|msg
argument_list|,
name|cause
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|msg
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"reason"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|causeMsg
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMessageWithThrowable
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|msg
init|=
literal|"msg"
decl_stmt|;
specifier|final
name|String
name|causeMsg
init|=
literal|"cause"
decl_stmt|;
name|Throwable
name|cause
init|=
operator|new
name|Throwable
argument_list|(
name|causeMsg
argument_list|)
decl_stmt|;
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ResourceAdapterInternalException
name|e
init|=
operator|new
name|ResourceAdapterInternalException
argument_list|(
name|msg
argument_list|,
name|cause
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|msg
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"reason"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|causeMsg
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMessageWithIteEx
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|msg
init|=
literal|"msg"
decl_stmt|;
specifier|final
name|String
name|causeMsg
init|=
literal|"cause"
decl_stmt|;
name|Exception
name|cause
init|=
operator|new
name|RuntimeException
argument_list|(
name|causeMsg
argument_list|)
decl_stmt|;
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ResourceAdapterInternalException
name|re
init|=
operator|new
name|ResourceAdapterInternalException
argument_list|(
name|msg
argument_list|,
operator|new
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
argument_list|(
name|cause
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|re
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|msg
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|re
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"reason"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|re
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|causeMsg
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|re
operator|.
name|getCause
argument_list|()
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMessageWithIteErroriNotThrow
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|msg
init|=
literal|"msg"
decl_stmt|;
specifier|final
name|String
name|causeMsg
init|=
literal|"cause"
decl_stmt|;
name|java
operator|.
name|lang
operator|.
name|Throwable
name|cause
init|=
operator|new
name|java
operator|.
name|lang
operator|.
name|UnknownError
argument_list|(
name|causeMsg
argument_list|)
decl_stmt|;
name|ResourceAdapterInternalException
name|re
init|=
operator|new
name|ResourceAdapterInternalException
argument_list|(
name|msg
argument_list|,
operator|new
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
argument_list|(
name|cause
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|re
operator|.
name|getCause
argument_list|()
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetLinkedExceptionReturnNullIfNoCause
parameter_list|()
throws|throws
name|Exception
block|{
name|ResourceAdapterInternalException
name|re
init|=
operator|new
name|ResourceAdapterInternalException
argument_list|(
literal|"ex"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"getLinkedException return null"
argument_list|,
name|re
operator|.
name|getLinkedException
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetLinkedExceptionReturnNullIfCauseIsError
parameter_list|()
throws|throws
name|Exception
block|{
name|java
operator|.
name|lang
operator|.
name|Throwable
name|cause
init|=
operator|new
name|java
operator|.
name|lang
operator|.
name|UnknownError
argument_list|(
literal|"error"
argument_list|)
decl_stmt|;
name|ResourceAdapterInternalException
name|re
init|=
operator|new
name|ResourceAdapterInternalException
argument_list|(
literal|"ex"
argument_list|,
name|cause
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"getLinkedException return null"
argument_list|,
name|re
operator|.
name|getLinkedException
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetLinkedExceptionReturnNotNullIfCauseIsException
parameter_list|()
throws|throws
name|Exception
block|{
name|java
operator|.
name|lang
operator|.
name|Throwable
name|cause
init|=
operator|new
name|RuntimeException
argument_list|(
literal|"runtime exception"
argument_list|)
decl_stmt|;
name|ResourceAdapterInternalException
name|re
init|=
operator|new
name|ResourceAdapterInternalException
argument_list|(
literal|"ex"
argument_list|,
name|cause
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"get same exception"
argument_list|,
name|cause
argument_list|,
name|re
operator|.
name|getLinkedException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

