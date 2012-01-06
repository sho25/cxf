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
name|rest
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
name|ArrayList
import|;
end_import

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
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|ServiceMode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
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
name|Document
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
name|helpers
operator|.
name|CastUtils
import|;
end_import

begin_class
annotation|@
name|WebServiceProvider
argument_list|()
annotation|@
name|ServiceMode
argument_list|(
name|value
operator|=
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
annotation|@
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingType
argument_list|(
name|value
operator|=
literal|"http://cxf.apache.org/bindings/xformat"
argument_list|)
specifier|public
class|class
name|RestSourcePayloadProvider
implements|implements
name|Provider
argument_list|<
name|DOMSource
argument_list|>
block|{
annotation|@
name|Resource
specifier|protected
name|WebServiceContext
name|wsContext
decl_stmt|;
specifier|public
name|RestSourcePayloadProvider
parameter_list|()
block|{     }
specifier|public
name|DOMSource
name|invoke
parameter_list|(
name|DOMSource
name|request
parameter_list|)
block|{
name|MessageContext
name|mc
init|=
name|wsContext
operator|.
name|getMessageContext
argument_list|()
decl_stmt|;
name|String
name|path
init|=
operator|(
name|String
operator|)
name|mc
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|PATH_INFO
argument_list|)
decl_stmt|;
name|String
name|query
init|=
operator|(
name|String
operator|)
name|mc
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|QUERY_STRING
argument_list|)
decl_stmt|;
name|String
name|httpMethod
init|=
operator|(
name|String
operator|)
name|mc
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
comment|/*Map<String, List<String>> requestHeader =              (Map<String, List<String>>)mc.get(MessageContext.HTTP_REQUEST_HEADERS);*/
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|responseHeader
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|mc
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|HTTP_RESPONSE_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseHeader
operator|==
literal|null
condition|)
block|{
name|responseHeader
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|mc
operator|.
name|put
argument_list|(
name|MessageContext
operator|.
name|HTTP_RESPONSE_HEADERS
argument_list|,
name|responseHeader
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|values
operator|.
name|add
argument_list|(
literal|"hello1"
argument_list|)
expr_stmt|;
name|values
operator|.
name|add
argument_list|(
literal|"hello2"
argument_list|)
expr_stmt|;
name|responseHeader
operator|.
name|put
argument_list|(
literal|"REST"
argument_list|,
name|values
argument_list|)
expr_stmt|;
comment|//        System.out.println("--path--- " + path);
comment|//        System.out.println("--query--- " + query);
comment|//        System.out.println("--httpMethod--- " + httpMethod);
if|if
condition|(
name|httpMethod
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"POST"
argument_list|)
condition|)
block|{
comment|// TBD: parse query info from DOMSource
comment|// System.out.println("--POST: getAllCustomers--- ");
return|return
name|getCustomer
argument_list|(
literal|null
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|httpMethod
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"GET"
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"/XMLService/RestProviderPort/Customer"
operator|.
name|equals
argument_list|(
name|path
argument_list|)
operator|&&
name|query
operator|==
literal|null
condition|)
block|{
comment|// System.out.println("--GET:getAllCustomers--- ");
return|return
name|getAllCustomers
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
literal|"/XMLService/RestProviderPort/Customer"
operator|.
name|equals
argument_list|(
name|path
argument_list|)
operator|&&
name|query
operator|!=
literal|null
condition|)
block|{
comment|// System.out.println("--GET:getCustomer--- ");
comment|// setup return context
return|return
name|getCustomer
argument_list|(
name|query
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|DOMSource
name|getAllCustomers
parameter_list|()
block|{
return|return
name|createDOMSource
argument_list|(
literal|"resources/CustomerAllResp.xml"
argument_list|)
return|;
block|}
specifier|private
name|DOMSource
name|getCustomer
parameter_list|(
name|String
name|customerID
parameter_list|)
block|{
return|return
name|createDOMSource
argument_list|(
literal|"resources/CustomerJohnResp.xml"
argument_list|)
return|;
block|}
specifier|private
name|DOMSource
name|createDOMSource
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|DocumentBuilderFactory
name|factory
decl_stmt|;
name|DocumentBuilder
name|builder
decl_stmt|;
name|Document
name|document
init|=
literal|null
decl_stmt|;
name|DOMSource
name|response
init|=
literal|null
decl_stmt|;
try|try
block|{
name|factory
operator|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|factory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|builder
operator|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
expr_stmt|;
name|InputStream
name|greetMeResponse
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|document
operator|=
name|builder
operator|.
name|parse
argument_list|(
name|greetMeResponse
argument_list|)
expr_stmt|;
name|response
operator|=
operator|new
name|DOMSource
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
block|}
end_class

end_unit

