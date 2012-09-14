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
name|jaxrs
operator|.
name|client
operator|.
name|spec
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
name|List
import|;
end_import

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientResponseContext
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
name|core
operator|.
name|MultivaluedMap
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
name|core
operator|.
name|Response
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
name|impl
operator|.
name|AbstractResponseContextImpl
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
name|impl
operator|.
name|MetadataMap
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
name|message
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|ClientResponseContextImpl
extends|extends
name|AbstractResponseContextImpl
implements|implements
name|ClientResponseContext
block|{
specifier|public
name|ClientResponseContextImpl
parameter_list|(
name|Response
name|r
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|super
argument_list|(
name|r
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
specifier|public
name|InputStream
name|getEntityStream
parameter_list|()
block|{
return|return
name|m
operator|.
name|get
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getHeaders
parameter_list|()
block|{
name|Object
name|headers
init|=
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
operator|)
name|headers
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
return|return
call|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
call|)
argument_list|(
name|MultivaluedMap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|)
name|r
operator|.
name|getHeaders
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setEntityStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|m
operator|.
name|put
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

