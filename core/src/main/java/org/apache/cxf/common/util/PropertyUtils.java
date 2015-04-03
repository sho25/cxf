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
name|common
operator|.
name|util
package|;
end_package

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
name|message
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * Holder of generic property related methods  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PropertyUtils
block|{
specifier|private
name|PropertyUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|boolean
name|isTrue
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
parameter_list|,
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|props
operator|==
literal|null
operator|||
name|key
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
name|isTrue
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|/**      * It might seem odd to return 'true' if a property == FALSE, but it      * is required sometimes.      *       * @param props      * @param key      * @return false if value is either the String "false" or Boolean.FALSE.  Otherwise returns      * true.      */
specifier|public
specifier|static
name|boolean
name|isFalse
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
parameter_list|,
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|props
operator|==
literal|null
operator|||
name|key
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
name|isFalse
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|/**      * Returns true if a value is either the String "true" (regardless of case)  or Boolean.TRUE.      * @param property      * @return true if value is either the String "true" or Boolean.TRUE.  Otherwise returns false.      */
specifier|public
specifier|static
name|boolean
name|isTrue
parameter_list|(
name|Object
name|property
parameter_list|)
block|{
if|if
condition|(
name|property
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|property
argument_list|)
operator|||
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|property
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * It might seem odd to return 'true' if a property == FALSE, but it is required sometimes.      *       * Returns false if a value is either the String "false" (regardless of case)  or Boolean.FALSE.      * @param property      * @return false if value is either the String "false" or Boolean.FALSE.  Otherwise returns      * true.      */
specifier|public
specifier|static
name|boolean
name|isFalse
parameter_list|(
name|Object
name|property
parameter_list|)
block|{
if|if
condition|(
name|property
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|Boolean
operator|.
name|FALSE
operator|.
name|equals
argument_list|(
name|property
argument_list|)
operator|||
literal|"false"
operator|.
name|equalsIgnoreCase
argument_list|(
name|property
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Long
name|getLong
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Long
condition|)
block|{
return|return
operator|(
name|Long
operator|)
name|o
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|Number
condition|)
block|{
return|return
operator|(
operator|(
name|Number
operator|)
name|o
operator|)
operator|.
name|longValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
return|return
name|Long
operator|.
name|valueOf
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|Integer
name|getInteger
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Integer
condition|)
block|{
return|return
operator|(
name|Integer
operator|)
name|o
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|Number
condition|)
block|{
return|return
operator|(
operator|(
name|Number
operator|)
name|o
operator|)
operator|.
name|intValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

