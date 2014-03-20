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
name|java
operator|.
name|util
operator|.
name|Set
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
name|Bean
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
name|Bus
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
name|Bus
name|bus
init|=
literal|null
decl_stmt|;
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
name|Set
argument_list|<
name|Bean
argument_list|<
name|?
argument_list|>
argument_list|>
name|candidates
init|=
name|beanManager
operator|.
name|getBeans
argument_list|(
name|CdiBusBean
operator|.
name|CXF
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|candidates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|Bean
argument_list|<
name|?
argument_list|>
name|candidate
init|=
name|candidates
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|bus
operator|=
operator|(
name|Bus
operator|)
name|beanManager
operator|.
name|getReference
argument_list|(
name|candidate
argument_list|,
name|Bus
operator|.
name|class
argument_list|,
name|beanManager
operator|.
name|createCreationalContext
argument_list|(
name|candidate
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
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

