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
name|type
operator|.
name|java5
operator|.
name|map
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
name|LinkedList
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
specifier|public
class|class
name|StudentServiceImpl
implements|implements
name|StudentService
block|{
specifier|private
name|Map
argument_list|<
name|Long
argument_list|,
name|Student
argument_list|>
name|studentMap
decl_stmt|;
specifier|public
name|StudentServiceImpl
parameter_list|()
block|{
name|studentMap
operator|=
operator|new
name|HashMap
argument_list|<
name|Long
argument_list|,
name|Student
argument_list|>
argument_list|()
expr_stmt|;
name|studentMap
operator|.
name|put
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|,
operator|new
name|Student
argument_list|(
literal|"Student1"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|studentMap
operator|.
name|put
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|100
argument_list|)
argument_list|,
operator|new
name|Student
argument_list|(
literal|"Student100"
argument_list|,
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|studentMap
operator|.
name|put
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
operator|-
literal|1
argument_list|)
argument_list|,
operator|new
name|Student
argument_list|(
literal|"StudentNegative"
argument_list|,
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Student
name|findStudent
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
return|return
name|studentMap
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|Student
argument_list|>
name|getStudents
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|filters
parameter_list|)
block|{
name|List
argument_list|<
name|Student
argument_list|>
name|returnValue
init|=
operator|new
name|LinkedList
argument_list|<
name|Student
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Long
argument_list|,
name|Student
argument_list|>
name|e
range|:
name|studentMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|filters
operator|.
name|containsKey
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|returnValue
operator|.
name|add
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|returnValue
return|;
block|}
specifier|public
name|List
argument_list|<
name|Student
argument_list|>
name|getStudentsByIds
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|)
block|{
name|List
argument_list|<
name|Student
argument_list|>
name|returnValue
init|=
operator|new
name|LinkedList
argument_list|<
name|Student
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|ids
control|)
block|{
name|Long
name|longId
init|=
name|Long
operator|.
name|decode
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Student
name|s
init|=
name|studentMap
operator|.
name|get
argument_list|(
name|longId
argument_list|)
decl_stmt|;
name|returnValue
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
return|return
name|returnValue
return|;
block|}
specifier|public
name|Map
argument_list|<
name|Long
argument_list|,
name|Student
argument_list|>
name|getStudentsMap
parameter_list|()
block|{
return|return
name|studentMap
return|;
block|}
block|}
end_class

end_unit

