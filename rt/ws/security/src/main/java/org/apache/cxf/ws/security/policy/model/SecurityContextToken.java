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
name|security
operator|.
name|policy
operator|.
name|model
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
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|security
operator|.
name|policy
operator|.
name|SPConstants
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
name|PolicyComponent
import|;
end_import

begin_comment
comment|/**  * Model class of SecurityContextToken assertion  */
end_comment

begin_class
specifier|public
class|class
name|SecurityContextToken
extends|extends
name|Token
block|{
name|boolean
name|requireExternalUriRef
decl_stmt|;
name|boolean
name|sc10SecurityContextToken
decl_stmt|;
specifier|public
name|SecurityContextToken
parameter_list|(
name|SPConstants
name|version
parameter_list|)
block|{
name|super
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return Returns the requireExternalUriRef.      */
specifier|public
name|boolean
name|isRequireExternalUriRef
parameter_list|()
block|{
return|return
name|requireExternalUriRef
return|;
block|}
comment|/**      * @param requireExternalUriRef The requireExternalUriRef to set.      */
specifier|public
name|void
name|setRequireExternalUriRef
parameter_list|(
name|boolean
name|requireExternalUriRef
parameter_list|)
block|{
name|this
operator|.
name|requireExternalUriRef
operator|=
name|requireExternalUriRef
expr_stmt|;
block|}
comment|/**      * @return Returns the sc10SecurityContextToken.      */
specifier|public
name|boolean
name|isSc10SecurityContextToken
parameter_list|()
block|{
return|return
name|sc10SecurityContextToken
return|;
block|}
comment|/**      * @param sc10SecurityContextToken The sc10SecurityContextToken to set.      */
specifier|public
name|void
name|setSc10SecurityContextToken
parameter_list|(
name|boolean
name|sc10SecurityContextToken
parameter_list|)
block|{
name|this
operator|.
name|sc10SecurityContextToken
operator|=
name|sc10SecurityContextToken
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      * @see org.apache.neethi.Assertion#getName()      */
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|constants
operator|.
name|getSecurityContextToken
argument_list|()
return|;
block|}
comment|/*      * (non-Javadoc)      * @see org.apache.neethi.Assertion#normalize()      */
specifier|public
name|PolicyComponent
name|normalize
parameter_list|()
block|{
comment|// TODO TODO Sanka
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"TODO Sanka"
argument_list|)
throw|;
block|}
comment|/*      * (non-Javadoc)      * @see org.apache.neethi.PolicyComponent#serialize(javax.xml.stream.XMLStreamWriter)      */
specifier|public
name|void
name|serialize
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|)
throws|throws
name|XMLStreamException
block|{
comment|// TODO TODO Sanka
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"TODO Sanka"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

