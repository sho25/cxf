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
name|continuations
package|;
end_package

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
name|UncheckedException
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
name|assertSame
import|;
end_import

begin_class
specifier|public
class|class
name|SuspendedInvocationExceptionTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testValidRuntimeException
parameter_list|()
block|{
name|Throwable
name|t
init|=
operator|new
name|UncheckedException
argument_list|(
operator|new
name|Throwable
argument_list|()
argument_list|)
decl_stmt|;
name|SuspendedInvocationException
name|ex
init|=
operator|new
name|SuspendedInvocationException
argument_list|(
name|t
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|t
argument_list|,
name|ex
operator|.
name|getRuntimeException
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t
argument_list|,
name|ex
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
name|testNoRuntimeException
parameter_list|()
block|{
name|SuspendedInvocationException
name|ex
init|=
operator|new
name|SuspendedInvocationException
argument_list|(
operator|new
name|Throwable
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|ex
operator|.
name|getRuntimeException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

