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
name|aegis
operator|.
name|util
operator|.
name|stax
package|;
end_package

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
name|Iterator
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|util
operator|.
name|NamespaceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jdom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jdom
operator|.
name|Namespace
import|;
end_import

begin_class
specifier|public
class|class
name|JDOMNamespaceContext
implements|implements
name|NamespaceContext
block|{
specifier|private
name|Element
name|element
decl_stmt|;
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|Namespace
name|namespace
init|=
name|element
operator|.
name|getNamespace
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|namespace
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|namespace
operator|.
name|getURI
argument_list|()
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|NamespaceHelper
operator|.
name|getPrefix
argument_list|(
name|element
argument_list|,
name|uri
argument_list|)
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|getPrefixes
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|prefixes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|NamespaceHelper
operator|.
name|getPrefixes
argument_list|(
name|element
argument_list|,
name|uri
argument_list|,
name|prefixes
argument_list|)
expr_stmt|;
return|return
name|prefixes
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|Element
name|getElement
parameter_list|()
block|{
return|return
name|element
return|;
block|}
specifier|public
name|void
name|setElement
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|this
operator|.
name|element
operator|=
name|element
expr_stmt|;
block|}
block|}
end_class

end_unit

