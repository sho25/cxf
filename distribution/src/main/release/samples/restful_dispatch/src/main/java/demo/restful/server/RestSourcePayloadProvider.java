begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|restful
operator|.
name|server
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
name|staxutils
operator|.
name|StaxUtils
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
name|Message
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
name|Message
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
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--path--- "
operator|+
name|path
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--query--- "
operator|+
name|query
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--httpMethod--- "
operator|+
name|httpMethod
argument_list|)
expr_stmt|;
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"---Invoking updateCustomer---"
argument_list|)
expr_stmt|;
return|return
name|updateCustomer
argument_list|(
name|request
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
name|path
operator|.
name|equals
argument_list|(
literal|"/customerservice/customer"
argument_list|)
operator|&&
name|query
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"---Invoking getAllCustomers---"
argument_list|)
expr_stmt|;
return|return
name|getAllCustomers
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|path
operator|.
name|equals
argument_list|(
literal|"/customerservice/customer"
argument_list|)
operator|&&
name|query
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"---Invoking getCustomer---"
argument_list|)
expr_stmt|;
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
literal|"/CustomerAllResp.xml"
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
literal|"/CustomerJohnResp.xml"
argument_list|)
return|;
block|}
specifier|private
name|DOMSource
name|updateCustomer
parameter_list|(
name|DOMSource
name|request
parameter_list|)
block|{
comment|// TBD: returned update customer info
return|return
name|createDOMSource
argument_list|(
literal|"/CustomerJohnResp.xml"
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
name|DOMSource
name|response
init|=
literal|null
decl_stmt|;
try|try
init|(
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
init|)
block|{
name|Document
name|document
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|greetMeResponse
argument_list|)
decl_stmt|;
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

