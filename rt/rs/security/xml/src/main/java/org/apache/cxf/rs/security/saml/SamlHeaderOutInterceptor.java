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
name|rs
operator|.
name|security
operator|.
name|saml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|logging
operator|.
name|LogUtils
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
name|DOMUtils
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
name|interceptor
operator|.
name|Fault
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
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SamlAssertionWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|util
operator|.
name|DOM2Writer
import|;
end_import

begin_class
specifier|public
class|class
name|SamlHeaderOutInterceptor
extends|extends
name|AbstractSamlOutInterceptor
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|SamlHeaderOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
try|try
block|{
name|SamlAssertionWrapper
name|assertionWrapper
init|=
name|createAssertion
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|Element
name|assertionElement
init|=
name|assertionWrapper
operator|.
name|toDOM
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|String
name|encodedToken
init|=
name|encodeToken
argument_list|(
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|assertionElement
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|getHeaders
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"SAML"
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|encodedToken
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Authorization"
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|RuntimeException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|", stacktrace: "
operator|+
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getHeaders
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
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
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
name|headers
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
return|return
name|headers
return|;
block|}
block|}
end_class

end_unit

