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
name|policy
operator|.
name|attachment
operator|.
name|reference
package|;
end_package

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
name|ws
operator|.
name|policy
operator|.
name|PolicyBuilder
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
name|ws
operator|.
name|policy
operator|.
name|PolicyConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|LocalDocumentReferenceResolver
implements|implements
name|ReferenceResolver
block|{
specifier|private
name|Document
name|document
decl_stmt|;
specifier|private
name|PolicyBuilder
name|builder
decl_stmt|;
specifier|public
name|LocalDocumentReferenceResolver
parameter_list|(
name|Document
name|di
parameter_list|,
name|PolicyBuilder
name|b
parameter_list|)
block|{
name|document
operator|=
name|di
expr_stmt|;
name|builder
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|Policy
name|resolveReference
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|resolveReference
argument_list|(
name|uri
argument_list|,
name|document
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Policy
name|resolveReference
parameter_list|(
name|String
name|uri
parameter_list|,
name|Element
name|el
parameter_list|)
block|{
if|if
condition|(
name|el
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|uri
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getAttributeNS
argument_list|(
name|PolicyConstants
operator|.
name|WSU_NAMESPACE_URI
argument_list|,
name|PolicyConstants
operator|.
name|WSU_ID_ATTR_NAME
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|builder
operator|.
name|getPolicy
argument_list|(
name|el
argument_list|)
return|;
block|}
name|Element
name|el2
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|el
argument_list|)
decl_stmt|;
while|while
condition|(
name|el2
operator|!=
literal|null
condition|)
block|{
name|Policy
name|p
init|=
name|resolveReference
argument_list|(
name|uri
argument_list|,
name|el2
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
return|return
name|p
return|;
block|}
name|el2
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|el2
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

