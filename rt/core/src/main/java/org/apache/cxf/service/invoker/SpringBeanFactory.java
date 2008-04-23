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
name|service
operator|.
name|invoker
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
name|message
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_comment
comment|/**  * Factory that will query the Spring ApplicationContext for the   * appropriate bean for each request.  *   * This can be expensive.  If the bean is "prototype" or similar such that a   * new instance is created each time, this could slow things down.  In that   * case, it's recommended to use this in conjunction with the PooledFactory  * to pool the beans or the SessionFactory or similar.  */
end_comment

begin_class
specifier|public
class|class
name|SpringBeanFactory
implements|implements
name|Factory
implements|,
name|ApplicationContextAware
block|{
name|ApplicationContext
name|ctx
decl_stmt|;
name|String
name|beanName
decl_stmt|;
specifier|public
name|SpringBeanFactory
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|beanName
operator|=
name|name
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|Object
name|create
parameter_list|(
name|Exchange
name|e
parameter_list|)
throws|throws
name|Throwable
block|{
return|return
name|ctx
operator|.
name|getBean
argument_list|(
name|beanName
argument_list|)
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|release
parameter_list|(
name|Exchange
name|e
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
comment|//nothing
block|}
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|arg0
parameter_list|)
throws|throws
name|BeansException
block|{
name|ctx
operator|=
name|arg0
expr_stmt|;
block|}
block|}
end_class

end_unit

