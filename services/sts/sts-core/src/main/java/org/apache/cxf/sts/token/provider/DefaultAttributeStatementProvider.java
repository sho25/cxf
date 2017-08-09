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
name|sts
operator|.
name|token
operator|.
name|provider
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
name|List
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
name|sts
operator|.
name|request
operator|.
name|TokenRequirements
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
name|WSS4JConstants
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
name|bean
operator|.
name|AttributeBean
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
name|bean
operator|.
name|AttributeStatementBean
import|;
end_import

begin_comment
comment|/**  * A default AttributeStatementProvider implementation. It simply creates a default attribute with  * value "authenticated".  */
end_comment

begin_class
specifier|public
class|class
name|DefaultAttributeStatementProvider
implements|implements
name|AttributeStatementProvider
block|{
comment|/**      * Get an AttributeStatementBean using the given parameters.      */
specifier|public
name|AttributeStatementBean
name|getStatement
parameter_list|(
name|TokenProviderParameters
name|providerParameters
parameter_list|)
block|{
name|AttributeStatementBean
name|attrBean
init|=
operator|new
name|AttributeStatementBean
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|AttributeBean
argument_list|>
name|attributeList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
name|String
name|tokenType
init|=
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
decl_stmt|;
name|AttributeBean
name|attributeBean
init|=
name|createDefaultAttribute
argument_list|(
name|tokenType
argument_list|)
decl_stmt|;
name|attributeList
operator|.
name|add
argument_list|(
name|attributeBean
argument_list|)
expr_stmt|;
name|attrBean
operator|.
name|setSamlAttributes
argument_list|(
name|attributeList
argument_list|)
expr_stmt|;
return|return
name|attrBean
return|;
block|}
comment|/**      * Create a default attribute      */
specifier|private
name|AttributeBean
name|createDefaultAttribute
parameter_list|(
name|String
name|tokenType
parameter_list|)
block|{
name|AttributeBean
name|attributeBean
init|=
operator|new
name|AttributeBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|WSS4JConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
operator|||
name|WSS4JConstants
operator|.
name|SAML_NS
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
condition|)
block|{
name|attributeBean
operator|.
name|setSimpleName
argument_list|(
literal|"token-requestor"
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|setQualifiedName
argument_list|(
literal|"http://cxf.apache.org/sts"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|attributeBean
operator|.
name|setQualifiedName
argument_list|(
literal|"token-requestor"
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|setNameFormat
argument_list|(
literal|"http://cxf.apache.org/sts"
argument_list|)
expr_stmt|;
block|}
name|attributeBean
operator|.
name|addAttributeValue
argument_list|(
literal|"authenticated"
argument_list|)
expr_stmt|;
return|return
name|attributeBean
return|;
block|}
block|}
end_class

end_unit

