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
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|model
operator|.
name|ClassResourceInfo
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
name|AcceptTypeQueryHandler
implements|implements
name|SystemQueryHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CONTENT_QUERY
init|=
literal|"_contentType"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|SHORTCUTS
decl_stmt|;
static|static
block|{
name|SHORTCUTS
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|SHORTCUTS
operator|.
name|put
argument_list|(
literal|"json"
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|SHORTCUTS
operator|.
name|put
argument_list|(
literal|"text"
argument_list|,
literal|"text/*"
argument_list|)
expr_stmt|;
name|SHORTCUTS
operator|.
name|put
argument_list|(
literal|"xml"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
comment|// more to come
block|}
specifier|public
name|Response
name|handleQuery
parameter_list|(
name|Message
name|m
parameter_list|,
name|ClassResourceInfo
name|rootResource
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queries
parameter_list|)
block|{
name|String
name|type
init|=
name|queries
operator|.
name|getFirst
argument_list|(
name|CONTENT_QUERY
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|SHORTCUTS
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|type
operator|=
name|SHORTCUTS
operator|.
name|get
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
name|String
name|types
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|)
decl_stmt|;
name|types
operator|=
name|types
operator|==
literal|null
condition|?
name|type
else|:
name|types
operator|+
literal|','
operator|+
name|type
expr_stmt|;
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
name|types
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|supports
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queries
parameter_list|)
block|{
return|return
name|queries
operator|.
name|getFirst
argument_list|(
name|CONTENT_QUERY
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

