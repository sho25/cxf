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
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|resource
operator|.
name|ExtendedURIResolver
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
name|cxf
operator|.
name|ws
operator|.
name|policy
operator|.
name|PolicyException
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
name|Constants
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
name|RemoteReferenceResolver
implements|implements
name|ReferenceResolver
block|{
specifier|private
name|String
name|baseURI
decl_stmt|;
specifier|private
name|PolicyBuilder
name|builder
decl_stmt|;
specifier|public
name|RemoteReferenceResolver
parameter_list|(
name|String
name|uri
parameter_list|,
name|PolicyBuilder
name|b
parameter_list|)
block|{
name|baseURI
operator|=
name|uri
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
name|int
name|pos
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|'#'
argument_list|)
decl_stmt|;
name|String
name|documentURI
init|=
name|pos
operator|==
operator|-
literal|1
condition|?
name|uri
else|:
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
decl_stmt|;
name|ExtendedURIResolver
name|resolver
init|=
operator|new
name|ExtendedURIResolver
argument_list|()
decl_stmt|;
name|InputSource
name|is
init|=
name|resolver
operator|.
name|resolve
argument_list|(
name|documentURI
argument_list|,
name|baseURI
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|is
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Document
name|doc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|doc
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|is
operator|.
name|getByteStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|PolicyException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
name|resolver
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|pos
operator|==
operator|-
literal|1
condition|)
block|{
return|return
name|builder
operator|.
name|getPolicy
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
return|;
block|}
name|String
name|id
init|=
name|uri
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|elem
range|:
name|PolicyConstants
operator|.
name|findAllPolicyElementsOfLocalName
argument_list|(
name|doc
argument_list|,
name|Constants
operator|.
name|ELEM_POLICY
argument_list|)
control|)
block|{
if|if
condition|(
name|id
operator|.
name|equals
argument_list|(
name|elem
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
name|elem
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

