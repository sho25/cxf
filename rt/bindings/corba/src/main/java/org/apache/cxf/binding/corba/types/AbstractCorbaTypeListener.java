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
name|binding
operator|.
name|corba
operator|.
name|types
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|NamespaceContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractCorbaTypeListener
implements|implements
name|CorbaTypeListener
block|{
specifier|protected
name|QName
name|currentElement
decl_stmt|;
specifier|protected
name|CorbaObjectHandler
name|handler
decl_stmt|;
specifier|protected
name|NamespaceContext
name|ctx
decl_stmt|;
specifier|public
name|AbstractCorbaTypeListener
parameter_list|(
name|CorbaObjectHandler
name|h
parameter_list|)
block|{
name|handler
operator|=
name|h
expr_stmt|;
block|}
specifier|public
name|void
name|setNamespaceContext
parameter_list|(
name|NamespaceContext
name|n
parameter_list|)
block|{
name|ctx
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|void
name|processStartElement
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|currentElement
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|void
name|processEndElement
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|currentElement
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|void
name|processCharacters
parameter_list|(
name|String
name|text
parameter_list|)
function_decl|;
specifier|public
name|CorbaObjectHandler
name|getCorbaObject
parameter_list|()
block|{
return|return
name|handler
return|;
block|}
specifier|public
name|void
name|processWriteAttribute
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|value
parameter_list|)
block|{     }
specifier|public
name|void
name|processWriteNamespace
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|namespaceURI
parameter_list|)
block|{     }
block|}
end_class

end_unit

