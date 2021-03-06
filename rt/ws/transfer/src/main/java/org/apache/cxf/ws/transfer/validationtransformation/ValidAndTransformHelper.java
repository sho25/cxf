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
name|transfer
operator|.
name|validationtransformation
package|;
end_package

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
name|ws
operator|.
name|transfer
operator|.
name|Representation
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
name|transfer
operator|.
name|shared
operator|.
name|faults
operator|.
name|InvalidRepresentation
import|;
end_import

begin_comment
comment|/**  * Helper class for validation and transformation.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ValidAndTransformHelper
block|{
specifier|private
name|ValidAndTransformHelper
parameter_list|()
block|{      }
comment|/**      * Validation and transformation process.      * @param resourceTypeIdentifiers List of resourceTypeIdentifiers.      * @param newRepresentation Incoming representation.      * @param oldRepresentation Representation stored in the ResourceManager.      */
specifier|public
specifier|static
name|void
name|validationAndTransformation
parameter_list|(
name|List
argument_list|<
name|ResourceTypeIdentifier
argument_list|>
name|resourceTypeIdentifiers
parameter_list|,
name|Representation
name|newRepresentation
parameter_list|,
name|Representation
name|oldRepresentation
parameter_list|)
block|{
if|if
condition|(
name|resourceTypeIdentifiers
operator|!=
literal|null
operator|&&
operator|!
name|resourceTypeIdentifiers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|ResourceTypeIdentifier
name|resourceIdentifier
range|:
name|resourceTypeIdentifiers
control|)
block|{
name|ResourceTypeIdentifierResult
name|valResult
init|=
name|resourceIdentifier
operator|.
name|identify
argument_list|(
name|newRepresentation
argument_list|)
decl_stmt|;
if|if
condition|(
name|valResult
operator|.
name|isCorrect
argument_list|()
condition|)
block|{
if|if
condition|(
name|valResult
operator|.
name|getTransformer
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ResourceTransformer
name|transformer
init|=
name|valResult
operator|.
name|getTransformer
argument_list|()
decl_stmt|;
name|ResourceValidator
name|validator
init|=
name|transformer
operator|.
name|transform
argument_list|(
name|newRepresentation
argument_list|,
name|oldRepresentation
argument_list|)
decl_stmt|;
if|if
condition|(
name|validator
operator|!=
literal|null
operator|&&
operator|!
name|validator
operator|.
name|validate
argument_list|(
name|newRepresentation
argument_list|,
name|oldRepresentation
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|InvalidRepresentation
argument_list|()
throw|;
block|}
block|}
return|return;
block|}
block|}
throw|throw
operator|new
name|InvalidRepresentation
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

