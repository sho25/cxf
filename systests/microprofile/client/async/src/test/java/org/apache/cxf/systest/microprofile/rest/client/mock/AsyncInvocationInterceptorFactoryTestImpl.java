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
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|mock
package|;
end_package

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
name|javax
operator|.
name|annotation
operator|.
name|Priority
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|ext
operator|.
name|AsyncInvocationInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|ext
operator|.
name|AsyncInvocationInterceptorFactory
import|;
end_import

begin_class
annotation|@
name|Priority
argument_list|(
literal|3500
argument_list|)
specifier|public
class|class
name|AsyncInvocationInterceptorFactoryTestImpl
implements|implements
name|AsyncInvocationInterceptorFactory
block|{
comment|//CHECKSTYLE:OFF
specifier|public
specifier|static
name|ThreadLocal
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|OUTBOUND
init|=
name|ThreadLocal
operator|.
name|withInitial
argument_list|(
parameter_list|()
lambda|->
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|()
return|;
block|}
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|ThreadLocal
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|INBOUND
init|=
name|ThreadLocal
operator|.
name|withInitial
argument_list|(
parameter_list|()
lambda|->
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|()
return|;
block|}
argument_list|)
decl_stmt|;
comment|//CHECKSTYLE:ON
specifier|static
class|class
name|AsyncInvocationInterceptorTestImpl
implements|implements
name|AsyncInvocationInterceptor
block|{
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|void
name|prepareContext
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|OUTBOUND
operator|.
name|get
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|AsyncInvocationInterceptorFactoryTestImpl
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|void
name|applyContext
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|INBOUND
operator|.
name|get
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|AsyncInvocationInterceptorFactoryTestImpl
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|void
name|removeContext
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|INBOUND
operator|.
name|get
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"REMOVE-"
operator|+
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"REMOVE-"
operator|+
name|AsyncInvocationInterceptorFactoryTestImpl
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|AsyncInvocationInterceptor
name|newInterceptor
parameter_list|()
block|{
return|return
operator|new
name|AsyncInvocationInterceptorTestImpl
argument_list|()
return|;
block|}
block|}
end_class

end_unit

