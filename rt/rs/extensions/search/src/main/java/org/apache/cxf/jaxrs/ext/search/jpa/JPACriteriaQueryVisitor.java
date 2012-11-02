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
name|ext
operator|.
name|search
operator|.
name|jpa
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|criteria
operator|.
name|CompoundSelection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|criteria
operator|.
name|CriteriaQuery
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|criteria
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|criteria
operator|.
name|Selection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|metamodel
operator|.
name|SingularAttribute
import|;
end_import

begin_class
specifier|public
class|class
name|JPACriteriaQueryVisitor
parameter_list|<
name|T
parameter_list|,
name|E
parameter_list|>
extends|extends
name|AbstractJPATypedQueryVisitor
argument_list|<
name|T
argument_list|,
name|E
argument_list|,
name|CriteriaQuery
argument_list|<
name|E
argument_list|>
argument_list|>
block|{
specifier|public
name|JPACriteriaQueryVisitor
parameter_list|(
name|EntityManager
name|em
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|tClass
parameter_list|,
name|Class
argument_list|<
name|E
argument_list|>
name|queryClass
parameter_list|)
block|{
name|this
argument_list|(
name|em
argument_list|,
name|tClass
argument_list|,
name|queryClass
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JPACriteriaQueryVisitor
parameter_list|(
name|EntityManager
name|em
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|tClass
parameter_list|,
name|Class
argument_list|<
name|E
argument_list|>
name|queryClass
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fieldMap
parameter_list|)
block|{
name|super
argument_list|(
name|em
argument_list|,
name|tClass
argument_list|,
name|queryClass
argument_list|,
name|fieldMap
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CriteriaQuery
argument_list|<
name|E
argument_list|>
name|getQuery
parameter_list|()
block|{
return|return
name|getCriteriaQuery
argument_list|()
return|;
block|}
specifier|public
name|CriteriaQuery
argument_list|<
name|E
argument_list|>
name|selectArray
parameter_list|(
name|List
argument_list|<
name|SingularAttribute
argument_list|<
name|T
argument_list|,
name|?
argument_list|>
argument_list|>
name|attributes
parameter_list|)
block|{
return|return
name|selectArraySelections
argument_list|(
name|toSelectionsArray
argument_list|(
name|toSelectionsList
argument_list|(
name|attributes
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|CriteriaQuery
argument_list|<
name|E
argument_list|>
name|selectArraySelections
parameter_list|(
name|Selection
argument_list|<
name|?
argument_list|>
modifier|...
name|selections
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|CompoundSelection
argument_list|<
name|E
argument_list|>
name|selection
init|=
operator|(
name|CompoundSelection
argument_list|<
name|E
argument_list|>
operator|)
name|getCriteriaBuilder
argument_list|()
operator|.
name|array
argument_list|(
name|selections
argument_list|)
decl_stmt|;
name|getQuery
argument_list|()
operator|.
name|select
argument_list|(
name|selection
argument_list|)
expr_stmt|;
return|return
name|getQuery
argument_list|()
return|;
block|}
specifier|public
name|CriteriaQuery
argument_list|<
name|E
argument_list|>
name|selectConstruct
parameter_list|(
name|List
argument_list|<
name|SingularAttribute
argument_list|<
name|T
argument_list|,
name|?
argument_list|>
argument_list|>
name|attributes
parameter_list|)
block|{
return|return
name|selectConstructSelections
argument_list|(
name|toSelectionsArray
argument_list|(
name|toSelectionsList
argument_list|(
name|attributes
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|CriteriaQuery
argument_list|<
name|E
argument_list|>
name|selectConstructSelections
parameter_list|(
name|Selection
argument_list|<
name|?
argument_list|>
modifier|...
name|selections
parameter_list|)
block|{
name|getQuery
argument_list|()
operator|.
name|select
argument_list|(
name|getCriteriaBuilder
argument_list|()
operator|.
name|construct
argument_list|(
name|getQueryClass
argument_list|()
argument_list|,
name|selections
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|getQuery
argument_list|()
return|;
block|}
specifier|public
name|CriteriaQuery
argument_list|<
name|E
argument_list|>
name|selectTuple
parameter_list|(
name|List
argument_list|<
name|SingularAttribute
argument_list|<
name|T
argument_list|,
name|?
argument_list|>
argument_list|>
name|attributes
parameter_list|)
block|{
return|return
name|selectTupleSelections
argument_list|(
name|toSelectionsArray
argument_list|(
name|toSelectionsList
argument_list|(
name|attributes
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|CriteriaQuery
argument_list|<
name|E
argument_list|>
name|selectTupleSelections
parameter_list|(
name|Selection
argument_list|<
name|?
argument_list|>
modifier|...
name|selections
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|CompoundSelection
argument_list|<
name|E
argument_list|>
name|selection
init|=
operator|(
name|CompoundSelection
argument_list|<
name|E
argument_list|>
operator|)
name|getCriteriaBuilder
argument_list|()
operator|.
name|tuple
argument_list|(
name|selections
argument_list|)
decl_stmt|;
name|getQuery
argument_list|()
operator|.
name|select
argument_list|(
name|selection
argument_list|)
expr_stmt|;
return|return
name|getQuery
argument_list|()
return|;
block|}
specifier|private
name|List
argument_list|<
name|Selection
argument_list|<
name|?
argument_list|>
argument_list|>
name|toSelectionsList
parameter_list|(
name|List
argument_list|<
name|SingularAttribute
argument_list|<
name|T
argument_list|,
name|?
argument_list|>
argument_list|>
name|attributes
parameter_list|)
block|{
name|List
argument_list|<
name|Selection
argument_list|<
name|?
argument_list|>
argument_list|>
name|selections
init|=
operator|new
name|ArrayList
argument_list|<
name|Selection
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|(
name|attributes
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|SingularAttribute
argument_list|<
name|T
argument_list|,
name|?
argument_list|>
name|attr
range|:
name|attributes
control|)
block|{
name|Path
argument_list|<
name|?
argument_list|>
name|path
init|=
name|getRoot
argument_list|()
operator|.
name|get
argument_list|(
name|attr
argument_list|)
decl_stmt|;
name|path
operator|.
name|alias
argument_list|(
name|attr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|selections
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
return|return
name|selections
return|;
block|}
specifier|private
specifier|static
name|Selection
argument_list|<
name|?
argument_list|>
index|[]
name|toSelectionsArray
parameter_list|(
name|List
argument_list|<
name|Selection
argument_list|<
name|?
argument_list|>
argument_list|>
name|selections
parameter_list|)
block|{
return|return
name|selections
operator|.
name|toArray
argument_list|(
operator|new
name|Selection
index|[
name|selections
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

