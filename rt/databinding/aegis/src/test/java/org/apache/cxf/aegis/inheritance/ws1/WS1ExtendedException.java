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
name|aegis
operator|.
name|inheritance
operator|.
name|ws1
package|;
end_package

begin_class
specifier|public
class|class
name|WS1ExtendedException
extends|extends
name|WS1Exception
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|346325404400568680L
decl_stmt|;
specifier|private
name|int
name|extendedCode
decl_stmt|;
specifier|public
name|WS1ExtendedException
parameter_list|()
block|{
name|extendedCode
operator|=
literal|0
expr_stmt|;
block|}
specifier|public
name|WS1ExtendedException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|extendedCode
operator|=
literal|0
expr_stmt|;
block|}
specifier|public
name|WS1ExtendedException
parameter_list|(
name|String
name|message
parameter_list|,
name|int
name|errorCode1
parameter_list|,
name|int
name|extendedCode
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|errorCode1
argument_list|,
name|object
argument_list|)
expr_stmt|;
name|this
operator|.
name|extendedCode
operator|=
name|extendedCode
expr_stmt|;
block|}
specifier|public
name|void
name|setExtendedCode
parameter_list|(
name|int
name|extendedCode
parameter_list|)
block|{
name|this
operator|.
name|extendedCode
operator|=
name|extendedCode
expr_stmt|;
block|}
specifier|public
name|int
name|getExtendedCode
parameter_list|()
block|{
return|return
name|extendedCode
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|super
operator|.
name|toString
argument_list|()
operator|+
literal|"; extendedCode="
operator|+
name|extendedCode
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|super
operator|.
name|equals
argument_list|(
name|o
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|WS1ExtendedException
name|that
init|=
operator|(
name|WS1ExtendedException
operator|)
name|o
decl_stmt|;
return|return
name|extendedCode
operator|==
name|that
operator|.
name|extendedCode
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|super
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|29
operator|*
name|result
operator|+
name|extendedCode
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

