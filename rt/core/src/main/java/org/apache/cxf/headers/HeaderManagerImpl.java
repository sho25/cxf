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
name|headers
package|;
end_package

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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
class|class
name|HeaderManagerImpl
implements|implements
name|HeaderManager
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|HeaderProcessor
argument_list|>
name|processors
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|HeaderProcessor
argument_list|>
argument_list|()
decl_stmt|;
name|Bus
name|bus
decl_stmt|;
specifier|public
name|HeaderManagerImpl
parameter_list|()
block|{     }
specifier|public
name|HeaderManagerImpl
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
annotation|@
name|Resource
specifier|public
specifier|final
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|bus
condition|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|HeaderManager
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|HeaderProcessor
name|getHeaderProcessor
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
if|if
condition|(
name|namespace
operator|==
literal|null
condition|)
block|{
name|namespace
operator|=
literal|""
expr_stmt|;
block|}
return|return
name|processors
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
return|;
block|}
specifier|public
name|void
name|registerHeaderProcessor
parameter_list|(
name|HeaderProcessor
name|processor
parameter_list|)
block|{
name|processors
operator|.
name|put
argument_list|(
name|processor
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

