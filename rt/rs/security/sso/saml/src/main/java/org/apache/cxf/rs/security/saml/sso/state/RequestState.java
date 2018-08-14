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
operator|.
name|sso
operator|.
name|state
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
specifier|public
class|class
name|RequestState
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|869323136115571943L
decl_stmt|;
specifier|private
name|String
name|targetAddress
decl_stmt|;
specifier|private
name|String
name|idpServiceAddress
decl_stmt|;
specifier|private
name|String
name|samlRequestId
decl_stmt|;
specifier|private
name|String
name|issuerId
decl_stmt|;
specifier|private
name|String
name|webAppContext
decl_stmt|;
specifier|private
name|String
name|webAppDomain
decl_stmt|;
specifier|private
name|long
name|createdAt
decl_stmt|;
specifier|private
name|long
name|timeToLive
decl_stmt|;
specifier|public
name|RequestState
parameter_list|()
block|{      }
comment|// CHECKSTYLE:OFF
specifier|public
name|RequestState
parameter_list|(
name|String
name|targetAddress
parameter_list|,
name|String
name|idpServiceAddress
parameter_list|,
name|String
name|samlRequestId
parameter_list|,
name|String
name|issuerId
parameter_list|,
name|String
name|webAppContext
parameter_list|,
name|String
name|webAppDomain
parameter_list|,
name|long
name|createdAt
parameter_list|,
name|long
name|timeToLive
parameter_list|)
block|{
name|this
operator|.
name|targetAddress
operator|=
name|targetAddress
expr_stmt|;
name|this
operator|.
name|idpServiceAddress
operator|=
name|idpServiceAddress
expr_stmt|;
name|this
operator|.
name|samlRequestId
operator|=
name|samlRequestId
expr_stmt|;
name|this
operator|.
name|issuerId
operator|=
name|issuerId
expr_stmt|;
name|this
operator|.
name|webAppContext
operator|=
name|webAppContext
expr_stmt|;
name|this
operator|.
name|webAppDomain
operator|=
name|webAppDomain
expr_stmt|;
name|this
operator|.
name|createdAt
operator|=
name|createdAt
expr_stmt|;
name|this
operator|.
name|timeToLive
operator|=
name|timeToLive
expr_stmt|;
block|}
comment|// CHECKSTYLE:ON
specifier|public
name|String
name|getTargetAddress
parameter_list|()
block|{
return|return
name|targetAddress
return|;
block|}
specifier|public
name|String
name|getIdpServiceAddress
parameter_list|()
block|{
return|return
name|idpServiceAddress
return|;
block|}
specifier|public
name|String
name|getSamlRequestId
parameter_list|()
block|{
return|return
name|samlRequestId
return|;
block|}
specifier|public
name|String
name|getIssuerId
parameter_list|()
block|{
return|return
name|issuerId
return|;
block|}
specifier|public
name|long
name|getCreatedAt
parameter_list|()
block|{
return|return
name|createdAt
return|;
block|}
specifier|public
name|long
name|getTimeToLive
parameter_list|()
block|{
return|return
name|timeToLive
return|;
block|}
specifier|public
name|String
name|getWebAppContext
parameter_list|()
block|{
return|return
name|webAppContext
return|;
block|}
specifier|public
name|String
name|getWebAppDomain
parameter_list|()
block|{
return|return
name|webAppDomain
return|;
block|}
block|}
end_class

end_unit

