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
name|ws
operator|.
name|mex
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
name|LinkedList
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|util
operator|.
name|StringUtils
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
name|util
operator|.
name|UrlUtils
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
name|endpoint
operator|.
name|Server
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
name|frontend
operator|.
name|WSDLGetUtils
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|phase
operator|.
name|PhaseInterceptorChain
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|MEXUtils
block|{
specifier|private
name|MEXUtils
parameter_list|()
block|{
comment|//utility
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Element
argument_list|>
name|getWSDLs
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|WSDLGetUtils
operator|.
name|AUTO_REWRITE_ADDRESS_ALL
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
name|base
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URL
argument_list|)
decl_stmt|;
name|String
name|ctxUri
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PATH_INFO
argument_list|)
decl_stmt|;
name|WSDLGetUtils
name|utils
init|=
operator|new
name|WSDLGetUtils
argument_list|()
decl_stmt|;
name|EndpointInfo
name|info
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|ret
init|=
operator|new
name|LinkedList
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|utils
operator|.
name|getWSDLIds
argument_list|(
name|message
argument_list|,
name|base
argument_list|,
name|ctxUri
argument_list|,
name|info
argument_list|)
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"wsdl"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|utils
operator|.
name|getDocument
argument_list|(
name|message
argument_list|,
name|base
argument_list|,
name|params
argument_list|,
name|ctxUri
argument_list|,
name|info
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getSchemaLocations
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|String
name|base
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URL
argument_list|)
decl_stmt|;
name|String
name|ctxUri
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PATH_INFO
argument_list|)
decl_stmt|;
name|WSDLGetUtils
name|utils
init|=
operator|new
name|WSDLGetUtils
argument_list|()
decl_stmt|;
name|EndpointInfo
name|info
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
return|return
name|utils
operator|.
name|getSchemaLocations
argument_list|(
name|message
argument_list|,
name|base
argument_list|,
name|ctxUri
argument_list|,
name|info
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Element
argument_list|>
name|getSchemas
parameter_list|(
name|Server
name|server
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|String
name|base
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URL
argument_list|)
decl_stmt|;
name|String
name|ctxUri
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PATH_INFO
argument_list|)
decl_stmt|;
name|WSDLGetUtils
name|utils
init|=
operator|new
name|WSDLGetUtils
argument_list|()
decl_stmt|;
name|EndpointInfo
name|info
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|locs
init|=
name|utils
operator|.
name|getSchemaLocations
argument_list|(
name|message
argument_list|,
name|base
argument_list|,
name|ctxUri
argument_list|,
name|info
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|ret
init|=
operator|new
name|LinkedList
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|xsd
range|:
name|locs
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|id
argument_list|)
operator|||
name|id
operator|.
name|equals
argument_list|(
name|xsd
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|query
init|=
name|xsd
operator|.
name|getValue
argument_list|()
operator|.
name|substring
argument_list|(
name|xsd
operator|.
name|getValue
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
name|UrlUtils
operator|.
name|parseQueryString
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|utils
operator|.
name|getDocument
argument_list|(
name|message
argument_list|,
name|base
argument_list|,
name|params
argument_list|,
name|ctxUri
argument_list|,
name|info
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getPolicyLocations
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Element
argument_list|>
name|getPolicies
parameter_list|(
name|Server
name|server
parameter_list|,
name|String
name|id
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

