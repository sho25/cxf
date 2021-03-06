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
name|maven_plugin
package|;
end_package

begin_comment
comment|/**  * Represents a wsdl file that is stored in a maven repository  */
end_comment

begin_class
specifier|public
class|class
name|WsdlArtifact
block|{
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
name|String
name|artifactId
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|classifier
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|public
name|WsdlArtifact
parameter_list|()
block|{
name|type
operator|=
literal|"wsdl"
expr_stmt|;
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * Not quite the same as equals: null in version      * matches anything.      * @param artifact      * @return      */
specifier|public
name|boolean
name|doesMatch
parameter_list|(
name|WsdlArtifact
name|artifact
parameter_list|)
block|{
if|if
condition|(
operator|!
name|type
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|groupId
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
operator|(
operator|(
name|classifier
operator|==
literal|null
operator|&&
name|artifact
operator|.
name|classifier
operator|==
literal|null
operator|)
operator|||
name|classifier
operator|!=
literal|null
operator|&&
name|classifier
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|classifier
argument_list|)
operator|)
operator|&&
name|artifactId
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|&&
operator|(
name|version
operator|==
literal|null
operator|||
name|version
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|)
return|;
block|}
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|classifier
return|;
block|}
specifier|public
name|void
name|setClassifier
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|artifactId
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|artifactId
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|classifier
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|classifier
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|groupId
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|groupId
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|type
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|type
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|version
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|version
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|WsdlArtifact
name|other
init|=
operator|(
name|WsdlArtifact
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|artifactId
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|artifactId
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|artifactId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|artifactId
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|classifier
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|classifier
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|classifier
operator|.
name|equals
argument_list|(
name|other
operator|.
name|classifier
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|groupId
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|groupId
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|groupId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|groupId
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|type
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|type
operator|.
name|equals
argument_list|(
name|other
operator|.
name|type
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|version
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|version
operator|.
name|equals
argument_list|(
name|other
operator|.
name|version
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"WsdlArtifact [groupId="
operator|+
name|groupId
operator|+
literal|", artifactId="
operator|+
name|artifactId
operator|+
literal|", version="
operator|+
name|version
operator|+
literal|", classifier="
operator|+
name|classifier
operator|+
literal|", type="
operator|+
name|type
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

