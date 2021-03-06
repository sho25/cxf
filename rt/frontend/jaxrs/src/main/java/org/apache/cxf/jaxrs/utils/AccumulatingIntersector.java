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
name|jaxrs
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_class
specifier|public
class|class
name|AccumulatingIntersector
implements|implements
name|MimeTypesIntersector
block|{
specifier|private
specifier|static
specifier|final
name|String
name|MEDIA_TYPE_DISTANCE_PARAM
init|=
literal|"d"
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|MediaType
argument_list|>
name|supportedMimeTypeList
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|addRequiredParamsIfPossible
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|addDistanceParameter
decl_stmt|;
name|AccumulatingIntersector
parameter_list|(
name|boolean
name|addRequiredParamsIfPossible
parameter_list|,
name|boolean
name|addDistanceParameter
parameter_list|)
block|{
name|this
operator|.
name|addRequiredParamsIfPossible
operator|=
name|addRequiredParamsIfPossible
expr_stmt|;
name|this
operator|.
name|addDistanceParameter
operator|=
name|addDistanceParameter
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|intersect
parameter_list|(
name|MediaType
name|requiredType
parameter_list|,
name|MediaType
name|userType
parameter_list|)
block|{
name|boolean
name|requiredTypeWildcard
init|=
name|requiredType
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|MediaType
operator|.
name|MEDIA_TYPE_WILDCARD
argument_list|)
decl_stmt|;
name|boolean
name|requiredSubTypeWildcard
init|=
name|requiredType
operator|.
name|getSubtype
argument_list|()
operator|.
name|contains
argument_list|(
name|MediaType
operator|.
name|MEDIA_TYPE_WILDCARD
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|requiredTypeWildcard
condition|?
name|userType
operator|.
name|getType
argument_list|()
else|:
name|requiredType
operator|.
name|getType
argument_list|()
decl_stmt|;
name|String
name|subtype
init|=
name|requiredSubTypeWildcard
condition|?
name|userType
operator|.
name|getSubtype
argument_list|()
else|:
name|requiredType
operator|.
name|getSubtype
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
init|=
name|userType
operator|.
name|getParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|addRequiredParamsIfPossible
condition|)
block|{
name|parameters
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|requiredType
operator|.
name|getParameters
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|parameters
operator|.
name|containsKey
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|parameters
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|addDistanceParameter
condition|)
block|{
name|int
name|distance
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|requiredTypeWildcard
condition|)
block|{
name|distance
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|requiredSubTypeWildcard
condition|)
block|{
name|distance
operator|++
expr_stmt|;
block|}
name|parameters
operator|.
name|put
argument_list|(
name|MEDIA_TYPE_DISTANCE_PARAM
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|distance
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|getSupportedMimeTypeList
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MediaType
argument_list|(
name|type
argument_list|,
name|subtype
argument_list|,
name|parameters
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|Set
argument_list|<
name|MediaType
argument_list|>
name|getSupportedMimeTypeList
parameter_list|()
block|{
return|return
name|supportedMimeTypeList
return|;
block|}
block|}
end_class

end_unit

