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
name|wsdl
package|;
end_package

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
name|addressing
operator|.
name|AttributedURIType
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
name|addressing
operator|.
name|EndpointReferenceType
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
name|addressing
operator|.
name|MetadataType
import|;
end_import

begin_comment
comment|/**  * Provides utility methods for obtaining endpoint references, wsdl definitions, etc.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|WSAEndpointReferenceUtils
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ANONYMOUS_ADDRESS
init|=
literal|"http://www.w3.org/2005/08/addressing/anonymous"
decl_stmt|;
specifier|static
specifier|final
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ObjectFactory
name|WSA_OBJECT_FACTORY
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ObjectFactory
argument_list|()
decl_stmt|;
specifier|private
name|WSAEndpointReferenceUtils
parameter_list|()
block|{
comment|// Utility class - never constructed
block|}
specifier|public
specifier|static
name|EndpointReferenceType
name|createEndpointReferenceWithMetadata
parameter_list|()
block|{
name|EndpointReferenceType
name|reference
init|=
name|WSA_OBJECT_FACTORY
operator|.
name|createEndpointReferenceType
argument_list|()
decl_stmt|;
name|reference
operator|.
name|setMetadata
argument_list|(
name|WSA_OBJECT_FACTORY
operator|.
name|createMetadataType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|reference
return|;
block|}
specifier|public
specifier|static
name|MetadataType
name|getSetMetadata
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|)
block|{
name|MetadataType
name|mt
init|=
name|ref
operator|.
name|getMetadata
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|mt
condition|)
block|{
name|mt
operator|=
name|WSA_OBJECT_FACTORY
operator|.
name|createMetadataType
argument_list|()
expr_stmt|;
name|ref
operator|.
name|setMetadata
argument_list|(
name|mt
argument_list|)
expr_stmt|;
block|}
return|return
name|mt
return|;
block|}
comment|/**      * Set the address of the provided endpoint reference.      * @param ref - the endpoint reference      * @param address - the address      */
specifier|public
specifier|static
name|void
name|setAddress
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|,
name|String
name|address
parameter_list|)
block|{
name|AttributedURIType
name|a
init|=
name|WSA_OBJECT_FACTORY
operator|.
name|createAttributedURIType
argument_list|()
decl_stmt|;
name|a
operator|.
name|setValue
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setAddress
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the address from the provided endpoint reference.      * @param ref - the endpoint reference      * @return String the address of the endpoint      */
specifier|public
specifier|static
name|String
name|getAddress
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|)
block|{
name|AttributedURIType
name|a
init|=
name|ref
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|a
condition|)
block|{
return|return
name|a
operator|.
name|getValue
argument_list|()
return|;
block|}
comment|// should wsdl be parsed for an address now?
return|return
literal|null
return|;
block|}
comment|/**      * Create a duplicate endpoint reference sharing all atributes      * @param ref the reference to duplicate      * @return EndpointReferenceType - the duplicate endpoint reference      */
specifier|public
specifier|static
name|EndpointReferenceType
name|duplicate
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|)
block|{
name|EndpointReferenceType
name|reference
init|=
name|WSA_OBJECT_FACTORY
operator|.
name|createEndpointReferenceType
argument_list|()
decl_stmt|;
name|reference
operator|.
name|setMetadata
argument_list|(
name|ref
operator|.
name|getMetadata
argument_list|()
argument_list|)
expr_stmt|;
name|reference
operator|.
name|getAny
argument_list|()
operator|.
name|addAll
argument_list|(
name|ref
operator|.
name|getAny
argument_list|()
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setAddress
argument_list|(
name|ref
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|reference
return|;
block|}
comment|/**      * Create an endpoint reference for the provided address.      * @param address - address URI      * @return EndpointReferenceType - the endpoint reference      */
specifier|public
specifier|static
name|EndpointReferenceType
name|getEndpointReference
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|EndpointReferenceType
name|reference
init|=
name|WSA_OBJECT_FACTORY
operator|.
name|createEndpointReferenceType
argument_list|()
decl_stmt|;
name|setAddress
argument_list|(
name|reference
argument_list|,
name|address
argument_list|)
expr_stmt|;
return|return
name|reference
return|;
block|}
specifier|public
specifier|static
name|EndpointReferenceType
name|getEndpointReference
parameter_list|(
name|AttributedURIType
name|address
parameter_list|)
block|{
name|EndpointReferenceType
name|reference
init|=
name|WSA_OBJECT_FACTORY
operator|.
name|createEndpointReferenceType
argument_list|()
decl_stmt|;
name|reference
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
return|return
name|reference
return|;
block|}
comment|/**      * Create an anonymous endpoint reference.      * @return EndpointReferenceType - the endpoint reference      */
specifier|public
specifier|static
name|EndpointReferenceType
name|getAnonymousEndpointReference
parameter_list|()
block|{
name|EndpointReferenceType
name|reference
init|=
name|WSA_OBJECT_FACTORY
operator|.
name|createEndpointReferenceType
argument_list|()
decl_stmt|;
name|setAddress
argument_list|(
name|reference
argument_list|,
name|ANONYMOUS_ADDRESS
argument_list|)
expr_stmt|;
return|return
name|reference
return|;
block|}
block|}
end_class

end_unit

