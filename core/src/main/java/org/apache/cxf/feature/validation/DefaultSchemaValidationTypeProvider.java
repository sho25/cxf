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
name|feature
operator|.
name|validation
package|;
end_package

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
name|Map
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
name|annotations
operator|.
name|SchemaValidation
operator|.
name|SchemaValidationType
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
name|service
operator|.
name|model
operator|.
name|OperationInfo
import|;
end_import

begin_comment
comment|/**  * Default provider which accepts a map of operation names to schema validation types.  The  * names ignore any namespaces  */
end_comment

begin_class
specifier|public
class|class
name|DefaultSchemaValidationTypeProvider
implements|implements
name|SchemaValidationTypeProvider
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|SchemaValidationType
argument_list|>
name|operationMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|DefaultSchemaValidationTypeProvider
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|SchemaValidationType
argument_list|>
name|operationMap
parameter_list|)
block|{
name|this
operator|.
name|operationMap
operator|.
name|putAll
argument_list|(
name|operationMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|SchemaValidationType
name|getSchemaValidationType
parameter_list|(
name|OperationInfo
name|info
parameter_list|)
block|{
name|SchemaValidationType
name|t
init|=
name|operationMap
operator|.
name|get
argument_list|(
name|info
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|t
operator|=
name|operationMap
operator|.
name|get
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
block|}
return|return
name|t
return|;
block|}
block|}
end_class

end_unit

