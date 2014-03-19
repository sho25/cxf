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
name|cdi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|BeanManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|CDI
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
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
name|BusFactory
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
name|transport
operator|.
name|servlet
operator|.
name|CXFNonSpringServlet
import|;
end_import

begin_comment
comment|/**  * Apache CXF servlet with CDI 1.1 integration support   */
end_comment

begin_class
specifier|public
class|class
name|CXFCdiServlet
extends|extends
name|CXFNonSpringServlet
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|2890970731778523861L
decl_stmt|;
specifier|private
name|boolean
name|busCreated
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|loadBus
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
block|{
specifier|final
name|BeanManager
name|beanManager
init|=
name|CDI
operator|.
name|current
argument_list|()
operator|.
name|getBeanManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|beanManager
operator|!=
literal|null
condition|)
block|{
specifier|final
name|JAXRSCdiResourceExtension
name|extension
init|=
name|beanManager
operator|.
name|getExtension
argument_list|(
name|JAXRSCdiResourceExtension
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|extension
operator|!=
literal|null
condition|)
block|{
name|setBus
argument_list|(
name|extension
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|busCreated
operator|=
literal|true
expr_stmt|;
name|setBus
argument_list|(
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|destroyBus
parameter_list|()
block|{
if|if
condition|(
name|busCreated
condition|)
block|{
comment|//if we created the Bus, we need to destroy it.  Otherwise, spring will handle it.
name|getBus
argument_list|()
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

