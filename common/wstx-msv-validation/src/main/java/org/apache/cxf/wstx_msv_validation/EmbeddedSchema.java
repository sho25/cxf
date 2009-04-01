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
name|wstx_msv_validation
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
name|Element
import|;
end_import

begin_comment
comment|/**  * A schema in a DOM Element. This is used in the WSDLSchemaReader to handle inter-schema cross-references. XS  */
end_comment

begin_class
specifier|public
class|class
name|EmbeddedSchema
block|{
specifier|private
name|String
name|systemId
decl_stmt|;
specifier|private
name|Element
name|schemaElement
decl_stmt|;
comment|/**      * Create object to represent one of the schemas in a WSDL      *       * @param systemId schema system Id.      * @param schemaElement Element for the schema.      */
specifier|public
name|EmbeddedSchema
parameter_list|(
name|String
name|systemId
parameter_list|,
name|Element
name|schemaElement
parameter_list|)
block|{
name|this
operator|.
name|systemId
operator|=
name|systemId
expr_stmt|;
name|this
operator|.
name|schemaElement
operator|=
name|schemaElement
expr_stmt|;
block|}
specifier|public
name|String
name|getSystemId
parameter_list|()
block|{
return|return
name|systemId
return|;
block|}
specifier|public
name|Element
name|getSchemaElement
parameter_list|()
block|{
return|return
name|schemaElement
return|;
block|}
block|}
end_class

end_unit

